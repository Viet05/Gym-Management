package com.group2.gymmanagement.repository;

import com.group2.gymmanagement.entities.Attendance;
import com.group2.gymmanagement.entities.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

  long countByDate(String date);

  List<Attendance> findByMember(User member);

  List<Attendance> findByTrainer(User trainer);
}
