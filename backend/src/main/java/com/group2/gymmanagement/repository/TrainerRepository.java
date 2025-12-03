package com.group2.gymmanagement.repository;

import com.group2.gymmanagement.entities.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing Trainer profile entities.
 */
@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

}
