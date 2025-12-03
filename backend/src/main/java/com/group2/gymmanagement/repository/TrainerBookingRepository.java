package com.group2.gymmanagement.repository;

import com.group2.gymmanagement.entities.TrainerBooking;
import com.group2.gymmanagement.entities.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing TrainerBooking entities.
 */
@Repository
public interface TrainerBookingRepository extends JpaRepository<TrainerBooking, Long> {

    // Find all bookings for a member
    List<TrainerBooking> findByMember(User member);

    // Find all bookings for a trainer
    List<TrainerBooking> findByTrainer(User trainer);
}
