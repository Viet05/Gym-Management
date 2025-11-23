package com.group2.gymmanagement.repository;

import com.group2.gymmanagement.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
