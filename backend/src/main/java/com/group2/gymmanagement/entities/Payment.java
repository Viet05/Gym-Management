package com.group2.gymmanagement.entities;

import com.group2.gymmanagement.enums.PaymentStatus;
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
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User member;

  @ManyToOne
  @JoinColumn(name = "id")
  private Subscription subscription;

  @Column(nullable = false)
  private Long amount;

  @Column
  private LocalDateTime paymentDate;

  @Column
  private String method;

  @Column(unique = true)
  private String reference;

  @Column
  private String transactionNo;

  @Column
  private String bankCode;

  @Column
  private String responseCode;

  @Enumerated(EnumType.STRING)
  private PaymentStatus status;

  @Column(columnDefinition = "TEXT")
  private String rawData;
}

