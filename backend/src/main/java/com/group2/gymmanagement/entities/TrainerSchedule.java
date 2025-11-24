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
@Table(name = "trainer_schedule")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainerSchedule {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long scheduleId;


  @ManyToOne
  @JoinColumn(name = "id")
  private User trainer;

  @Column(name = "date")
  private String date;

  @Column(name = "time_slot")
  private String timeSlot;

  @Column(name = "description")
  private String description;
}
