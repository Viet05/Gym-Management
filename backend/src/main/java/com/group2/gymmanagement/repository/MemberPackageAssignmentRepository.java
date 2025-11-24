package com.group2.gymmanagement.repository;

import com.group2.gymmanagement.entities.MemberPackageAssigment;

import com.group2.gymmanagement.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MemberPackageAssignmentRepository extends JpaRepository<MemberPackageAssigment, Long> {

  List<MemberPackageAssigment> findByMember(User member);

  List<MemberPackageAssigment> findByActiveStatus(Boolean activeStatus);
}
