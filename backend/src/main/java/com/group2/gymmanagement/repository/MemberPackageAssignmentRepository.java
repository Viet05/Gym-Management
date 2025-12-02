package com.group2.gymmanagement.repository;

import com.group2.gymmanagement.entities.MemberPackageAssigment;

import com.group2.gymmanagement.entities.MembershipPackage;
import com.group2.gymmanagement.entities.User;
import com.group2.gymmanagement.enums.PackageStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MemberPackageAssignmentRepository extends JpaRepository<MemberPackageAssigment, Long> {

  List<MemberPackageAssigment> findByMember(User member);

  List<MemberPackageAssigment> findByActiveStatus(PackageStatus activeStatus);

  Optional<MemberPackageAssigment> findByMemberAndMembershipPackageAndActiveStatus(User member, MembershipPackage memberPackageAssigment, PackageStatus packageStatus);
}
