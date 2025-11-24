package com.group2.gymmanagement.Specification;

import com.group2.gymmanagement.entities.User;
import jakarta.persistence.criteria.Predicate;
import java.util.Map;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSpecification {

  public Specification<User> getUserSpecification(Map<String, Objects> request) {
    return ((root, query, cb) -> {
      Predicate predicate = cb.conjunction();

      if (request.containsKey("name") && !request.get("name").toString().isEmpty()) {
        predicate = cb.and(predicate, cb.like(root.get("name"), "%" + request.get("name") + "%"));
      }

      if (request.containsKey("email") && !request.get("email").toString().isEmpty()) {
        predicate = cb.and(predicate, cb.equal(root.get("email"), request.get("email").toString()));
      }

      if (request.containsKey("role") && !request.get("role").toString().isEmpty()) {
        predicate = cb.and(predicate, cb.like(root.get("role"), "%" + request.get("role").toString().toUpperCase() + "%"));
      }

      if (request.containsKey("fullName") && !request.get("fullName").toString().isEmpty()) {
        predicate = cb.and(predicate, cb.like(root.get("fullName"), "%" + request.get("fullName").toString() + "%"));
      }

      if (request.containsKey("phone") && !request.get("phone").toString().isEmpty()) {
        predicate = cb.and(predicate, cb.equal(root.get("phone"), request.get("phone").toString()));
      }

      return predicate;
    });
  }
}
