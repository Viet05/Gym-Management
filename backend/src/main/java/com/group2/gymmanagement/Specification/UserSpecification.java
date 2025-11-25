package com.group2.gymmanagement.Specification;

import com.group2.gymmanagement.entities.User;
import com.group2.gymmanagement.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSpecification {

  private final UserRepository userRepository;

  public List<User> getUsersByFilter(Map<String, Object> filters) {
    Specification<User> spec = (root, query, cb) -> {
      Predicate predicate = cb.conjunction();

      if (filters.containsKey("name") && filters.get("name") != null && !filters.get("name").toString().isEmpty()) {
        predicate = cb.and(predicate, cb.like(root.get("name"), "%" + filters.get("name") + "%"));
      }

      if (filters.containsKey("email") && filters.get("email") != null && !filters.get("email").toString().isEmpty()) {
        predicate = cb.and(predicate, cb.equal(root.get("email"), filters.get("email").toString()));
      }

      if (filters.containsKey("role") && filters.get("role") != null && !filters.get("role").toString().isEmpty()) {
        predicate = cb.and(predicate,
            cb.like(root.get("role"), "%" + filters.get("role").toString().toUpperCase() + "%"));
      }

      if (filters.containsKey("fullName") && filters.get("fullName") != null
          && !filters.get("fullName").toString().isEmpty()) {
        predicate = cb.and(predicate, cb.like(root.get("fullName"), "%" + filters.get("fullName") + "%"));
      }

      if (filters.containsKey("phone") && filters.get("phone") != null && !filters.get("phone").toString().isEmpty()) {
        predicate = cb.and(predicate, cb.equal(root.get("phone"), filters.get("phone").toString()));
      }

      return predicate;
    };

    return userRepository.findAll(spec);
  }
}
