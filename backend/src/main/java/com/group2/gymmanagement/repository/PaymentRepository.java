package com.group2.gymmanagement.repository;

import com.group2.gymmanagement.entities.Payment;
import com.group2.gymmanagement.entities.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

  List<Payment> findByMember(User member);
}
