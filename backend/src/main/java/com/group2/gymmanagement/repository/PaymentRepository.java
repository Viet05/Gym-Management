package com.group2.gymmanagement.repository;

import com.group2.gymmanagement.entities.Payment;
import com.group2.gymmanagement.entities.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

  @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'COMPLETED'")
  Double sumTotalRevenue();

  List<Payment> findByMember(User member);

  List<Payment> findByMemberOrderByCreatedAtDesc(User member);

  Page<Payment> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
