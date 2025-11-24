package com.group2.gymmanagement.repository;

import com.group2.gymmanagement.entities.User;
import com.group2.gymmanagement.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUserName(String userName);

  boolean existsByUserName(String userName);

  List<User> findByRole(UserRole role);

  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);
}
