package com.group2.gymmanagement.repository;

import com.group2.gymmanagement.entities.MemberTrainerAssignment;
import com.group2.gymmanagement.entities.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberTrainerAssignmentRepository extends
    JpaRepository<MemberTrainerAssignment, Long> {

  List<MemberTrainerAssignment> findByMember(User member);

  List<MemberTrainerAssignment> findByTrainer(User trainer);
}
