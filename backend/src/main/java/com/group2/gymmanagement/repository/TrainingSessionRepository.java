package com.group2.gymmanagement.repository;

import com.group2.gymmanagement.entities.TrainingSession;
import com.group2.gymmanagement.entities.User;
import com.group2.gymmanagement.enums.TrainingSessionStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {

  @Query("""
      select case when count(s) > 0 then true else false end
      from TrainingSession s
      where s.trainer = :trainer and s.sessionDate = :date and s.status = :status
        and s.startTime < :endTime and s.endTime > :startTime
      """)
  boolean hasTrainerOverlap(@Param("trainer") User trainer, @Param("date") LocalDate date,
      @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime,
      @Param("status") TrainingSessionStatus status);

  @Query("""
      select case when count(s) > 0 then true else false end
      from TrainingSession s
      where s.member = :member and s.sessionDate = :date and s.status = :status
        and s.startTime < :endTime and s.endTime > :startTime
      """)
  boolean hasMemberOverlap(@Param("member") User member, @Param("date") LocalDate date,
      @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime,
      @Param("status") TrainingSessionStatus status);

  long countByTrainerAndSessionDateAndStatus(User trainer, LocalDate date, TrainingSessionStatus status);

  List<TrainingSession> findByTrainerIdAndSessionDateOrderByStartTime(Long trainerId, LocalDate sessionDate);
}
