package com.group2.gymmanagement.repository;

import com.group2.gymmanagement.entities.GymPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GymPlanRepository extends JpaRepository<GymPlan, Long> {
    boolean existsByName(String name);
}
