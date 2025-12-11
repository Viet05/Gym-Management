package com.group2.gymmanagement.repository;

import com.group2.gymmanagement.entities.Payment;
import com.group2.gymmanagement.entities.Subscription;
import com.group2.gymmanagement.entities.User;
import com.group2.gymmanagement.enums.PaymentStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

  @Query("SELECT SUM(p.amount) FROM Payment p")
  Double sumTotalRevenue();

  List<Payment> findByMemberId(Long memberId);

  Optional<Payment> findByReference(String reference);

  Optional<Payment> findBySubscriptionId(Long subscriptionId);

  List<Payment> findByStatus(PaymentStatus status);

  Optional<Payment> findByTransactionNo(String transactionNo);

  Optional<Payment> findBySubscriptionAndStatus(Subscription subscription, PaymentStatus paymentStatus);
}
