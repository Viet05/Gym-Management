package com.group2.gymmanagement.repository;

import com.group2.gymmanagement.entities.User;
import com.group2.gymmanagement.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

  Optional<User> findByUsername(String username);

  boolean existsByUsername(String username);

  List<User> findByRole(UserRole role);

  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

  long countByRole(UserRole role);

  long countByStatus(String status);
}
