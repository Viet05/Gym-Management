package com.group2.gymmanagement.entities;

import com.group2.gymmanagement.enums.PackageStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_package_assigment")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberPackageAssigment implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "id")
  private User member;

  @ManyToOne
  @JoinColumn(name = "id")
  private MembershipPackage membershipPackage;

  @Column(name = "start_date")
  private LocalDateTime startDate;

  @Column(name = "end_date")
  private LocalDateTime endDate;

  @Column(name = "active_status")
  @Enumerated(EnumType.STRING)
  private PackageStatus activeStatus;
}
