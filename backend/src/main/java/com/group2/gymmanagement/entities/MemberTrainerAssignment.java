package com.group2.gymmanagement.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_trainer_assignment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberTrainerAssignment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long assignmentId;

  @ManyToOne
  @JoinColumn(name = "id")
  private User member;

  @ManyToOne
  @JoinColumn(name = "trainer_id")
  private User trainer;

  @Column(name = "start_date")
  private String startDate;

  @Column(name = "end_date")
  private String endDate;
}
