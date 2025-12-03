package com.group2.gymmanagement.repository;

import com.group2.gymmanagement.entities.Schedule;
import com.group2.gymmanagement.entities.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing trainer Schedule entities.
 */
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // Find all schedules for a specific trainer
    List<Schedule> findByTrainer(User trainer);
}
