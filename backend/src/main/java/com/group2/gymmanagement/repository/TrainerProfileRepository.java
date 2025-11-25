package com.group2.gymmanagement.repository;

import com.group2.gymmanagement.entities.TrainerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerProfileRepository extends JpaRepository<TrainerProfile, Integer> {

}
