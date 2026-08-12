package com.group2.gymmanagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.group2.gymmanagement.dto.request.AutoScheduleRequest;
import com.group2.gymmanagement.dto.response.TrainingSessionDTO;
import com.group2.gymmanagement.entities.TrainingSession;
import com.group2.gymmanagement.entities.User;
import com.group2.gymmanagement.enums.TrainingSessionStatus;
import com.group2.gymmanagement.enums.UserRole;
import com.group2.gymmanagement.repository.TrainingSessionRepository;
import com.group2.gymmanagement.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

class TrainingSessionServiceTest {

  private final UserRepository userRepository = mock(UserRepository.class);
  private final TrainingSessionRepository sessionRepository = mock(TrainingSessionRepository.class);
  private final StringRedisTemplate redisTemplate = mock(StringRedisTemplate.class);
  private final ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
  private final TrainingSessionService service = new TrainingSessionService(
      userRepository, sessionRepository, redisTemplate);

  @BeforeEach
  void setUp() {
    when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    when(valueOperations.setIfAbsent(any(), any(), any())).thenReturn(true);
  }

  @Test
  void autoScheduleSelectsAvailableTrainerWithLowestDailyLoad() {
    LocalDate date = LocalDate.now().plusDays(1);
    AutoScheduleRequest request = request(date);
    User member = user(1L, "Member", UserRole.MEMBER);
    User busyTrainer = user(2L, "Busy", UserRole.TRAINER);
    User availableTrainer = user(3L, "Available", UserRole.TRAINER);
    TrainingSession saved = TrainingSession.builder().id(99L).member(member).trainer(availableTrainer)
        .sessionDate(date).startTime(request.getStartTime()).endTime(request.getEndTime())
        .status(TrainingSessionStatus.BOOKED).build();

    when(userRepository.findById(1L)).thenReturn(Optional.of(member));
    when(userRepository.findByRole(UserRole.TRAINER)).thenReturn(List.of(busyTrainer, availableTrainer));
    when(sessionRepository.hasMemberOverlap(eq(member), eq(date), any(), any(), any())).thenReturn(false);
    when(sessionRepository.hasTrainerOverlap(any(), eq(date), any(), any(), any())).thenReturn(false);
    when(sessionRepository.countByTrainerAndSessionDateAndStatus(busyTrainer, date,
        TrainingSessionStatus.BOOKED)).thenReturn(4L);
    when(sessionRepository.countByTrainerAndSessionDateAndStatus(availableTrainer, date,
        TrainingSessionStatus.BOOKED)).thenReturn(1L);
    when(sessionRepository.save(any(TrainingSession.class))).thenReturn(saved);

    TrainingSessionDTO result = service.autoSchedule(request);

    assertThat(result.getTrainerId()).isEqualTo(3L);
    assertThat(result.getStatus()).isEqualTo("BOOKED");
  }

  private AutoScheduleRequest request(LocalDate date) {
    AutoScheduleRequest request = new AutoScheduleRequest();
    request.setMemberId(1L);
    request.setSessionDate(date);
    request.setStartTime(LocalTime.of(9, 0));
    request.setEndTime(LocalTime.of(10, 0));
    return request;
  }

  private User user(Long id, String fullName, UserRole role) {
    return User.builder().id(id).fullName(fullName).role(role).status("1").build();
  }
}
