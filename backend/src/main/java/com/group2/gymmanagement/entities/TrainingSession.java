package com.group2.gymmanagement.entities;

import com.group2.gymmanagement.enums.TrainingSessionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "training_session", indexes = {
    @Index(name = "idx_training_session_trainer_date", columnList = "trainer_id, session_date"),
    @Index(name = "idx_training_session_member_date", columnList = "member_id, session_date")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingSession {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "trainer_id", nullable = false)
  private User trainer;

  @ManyToOne(optional = false)
  @JoinColumn(name = "member_id", nullable = false)
  private User member;

  @Column(name = "session_date", nullable = false)
  private LocalDate sessionDate;

  @Column(name = "start_time", nullable = false)
  private LocalTime startTime;

  @Column(name = "end_time", nullable = false)
  private LocalTime endTime;

  @Column(length = 500)
  private String notes;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TrainingSessionStatus status;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;
}
