package com.group2.gymmanagement.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "membership_package")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MembershipPackage implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "duration_month")
  private Long durationMonth;

  @Column(name = "price")
  private Double price;

  @Column(name = "description")
  private String description;

  @CreationTimestamp
  @Column(name = "created")
  private LocalDateTime createdDate;

  @UpdateTimestamp
  @Column(name = "updated")
  private LocalDateTime updatedDate;
}
