package com.group2.gymmanagement.entities;

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
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long paymentId;

  /** BUG FIX: was @JoinColumn(name = "id") which is ambiguous; must be "member_id". */
  @ManyToOne
  @JoinColumn(name = "member_id")
  private User member;

  @ManyToOne
  @JoinColumn(name = "package_id")
  private MembershipPackage membershipPackage;

  @Column(name = "amount")
  private Double amount;

  @Column(name = "payment_date")
  private LocalDate paymentDate;

  @Column(name = "method")
  private String method;

  @Column(name = "reference")
  private String reference;

  @Column(name = "description", length = 500)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  @Builder.Default
  private PaymentStatus status = PaymentStatus.COMPLETED;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  public enum PaymentStatus {
    PENDING, COMPLETED, CANCELLED
  }
}
