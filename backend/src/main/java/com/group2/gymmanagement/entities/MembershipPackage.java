package com.group2.gymmanagement.entities;

import com.group2.gymmanagement.enums.PackageStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
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

  @Column(name = "active")
  @Enumerated(EnumType.STRING)
  private PackageStatus active;

  @CreationTimestamp
  @Column(name = "created")
  private LocalDateTime createdDate;

  @UpdateTimestamp
  @Column(name = "updated")
  private LocalDateTime updatedDate;

  @OneToMany(mappedBy = "membershipPackage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<MemberPackageAssigment> packageAssigments;
}
