package com.group2.gymmanagement.repository;

import com.group2.gymmanagement.entities.TrainerSchedule;
import com.group2.gymmanagement.entities.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerScheduleRepository extends JpaRepository<TrainerSchedule, Long> {

  List<TrainerSchedule> findByTrainer(User trainer);
}
