package com.group2.gymmanagement.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trainer_profile")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainerProfile {

  @Id
  private Long id;   // PK = user_id

  @OneToOne
  @MapsId          // <-- Dùng user.id làm PK
  @JoinColumn(name = "id")
  private User user;

  @Column(name = "shift")
  private String shift;

  @Column(name = "specialization")
  private String specialization;

  @Column(name = "completed")
  private Boolean completed;

  @Column(name = "experience_years")
  private Integer experienceYears;
}

