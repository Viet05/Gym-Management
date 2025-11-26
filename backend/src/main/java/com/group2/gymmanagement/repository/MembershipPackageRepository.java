package com.group2.gymmanagement.repository;

import com.group2.gymmanagement.entities.MembershipPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipPackageRepository extends JpaRepository<MembershipPackage, Long> {

  boolean existsByName(String name);
}
