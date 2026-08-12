package com.group2.gymmanagement.service;

import com.group2.gymmanagement.dto.request.AutoScheduleRequest;
import com.group2.gymmanagement.dto.response.TrainingSessionDTO;
import com.group2.gymmanagement.entities.TrainingSession;
import com.group2.gymmanagement.entities.User;
import com.group2.gymmanagement.enums.TrainingSessionStatus;
import com.group2.gymmanagement.enums.UserRole;
import com.group2.gymmanagement.repository.TrainingSessionRepository;
import com.group2.gymmanagement.repository.UserRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TrainingSessionService {

  private static final Duration SCHEDULING_LOCK_TTL = Duration.ofSeconds(15);

  private final UserRepository userRepository;
  private final TrainingSessionRepository trainingSessionRepository;
  private final StringRedisTemplate stringRedisTemplate;

  /**
   * Allocates the least-loaded available trainer. The Redis lock serializes competing requests for
   * the same member/time window; MySQL overlap checks remain the source of truth.
   */
  @Transactional
  @CacheEvict(cacheNames = "trainer-sessions", allEntries = true)
  public TrainingSessionDTO autoSchedule(AutoScheduleRequest request) {
    validateTimeRange(request);
    String lockKey = lockKey(request);
    String lockValue = UUID.randomUUID().toString();

    Boolean locked = stringRedisTemplate.opsForValue()
        .setIfAbsent(lockKey, lockValue, SCHEDULING_LOCK_TTL);
    if (!Boolean.TRUE.equals(locked)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT,
          "A scheduling request for this member and time is already being processed");
    }

    try {
      User member = userRepository.findById(request.getMemberId())
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));
      if (member.getRole() != UserRole.MEMBER) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The selected user is not a member");
      }

      if (trainingSessionRepository.hasMemberOverlap(member, request.getSessionDate(),
          request.getStartTime(), request.getEndTime(), TrainingSessionStatus.BOOKED)) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Member already has an overlapping session");
      }

      User trainer = userRepository.findByRole(UserRole.TRAINER).stream()
          .filter(this::isActive)
          .filter(candidate -> !trainingSessionRepository.hasTrainerOverlap(candidate,
              request.getSessionDate(), request.getStartTime(), request.getEndTime(),
              TrainingSessionStatus.BOOKED))
          .min(Comparator.comparingLong(candidate -> trainingSessionRepository.countByTrainerAndSessionDateAndStatus(
              candidate, request.getSessionDate(), TrainingSessionStatus.BOOKED)))
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,
              "No trainer is available for the requested time"));

      TrainingSession saved = trainingSessionRepository.save(TrainingSession.builder()
          .member(member)
          .trainer(trainer)
          .sessionDate(request.getSessionDate())
          .startTime(request.getStartTime())
          .endTime(request.getEndTime())
          .notes(request.getNotes())
          .status(TrainingSessionStatus.BOOKED)
          .build());
      return toDto(saved);
    } finally {
      releaseLock(lockKey, lockValue);
    }
  }

  @Cacheable(cacheNames = "trainer-sessions", key = "'trainer:' + #trainerId + ':' + #date")
  @Transactional(readOnly = true)
  public List<TrainingSessionDTO> getTrainerSchedule(Long trainerId, LocalDate date) {
    if (!userRepository.existsById(trainerId)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not found");
    }
    return trainingSessionRepository.findByTrainerIdAndSessionDateOrderByStartTime(trainerId, date)
        .stream().map(this::toDto).toList();
  }

  private void validateTimeRange(AutoScheduleRequest request) {
    if (!request.getEndTime().isAfter(request.getStartTime())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End time must be after start time");
    }
  }

  private boolean isActive(User user) {
    return "1".equals(user.getStatus()) || "ACTIVE".equalsIgnoreCase(user.getStatus());
  }

  private String lockKey(AutoScheduleRequest request) {
    return "schedule-lock:member:" + request.getMemberId() + ":" + request.getSessionDate()
        + ":" + request.getStartTime() + ":" + request.getEndTime();
  }

  private void releaseLock(String lockKey, String lockValue) {
    String currentValue = stringRedisTemplate.opsForValue().get(lockKey);
    if (lockValue.equals(currentValue)) {
      stringRedisTemplate.delete(lockKey);
    }
  }

  private TrainingSessionDTO toDto(TrainingSession session) {
    return TrainingSessionDTO.builder()
        .id(session.getId())
        .trainerId(session.getTrainer().getId())
        .trainerName(session.getTrainer().getFullName())
        .memberId(session.getMember().getId())
        .memberName(session.getMember().getFullName())
        .sessionDate(session.getSessionDate())
        .startTime(session.getStartTime())
        .endTime(session.getEndTime())
        .notes(session.getNotes())
        .status(session.getStatus().name())
        .build();
  }
}
