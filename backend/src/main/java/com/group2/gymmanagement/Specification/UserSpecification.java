package com.group2.gymmanagement.Specification;

import com.group2.gymmanagement.entities.User;
import com.group2.gymmanagement.enums.UserRole;
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

      // BUG-1 FIX: "name" does not exist on User entity — the correct field is "fullName"
      if (filters.containsKey("name") && filters.get("name") != null && !filters.get("name").toString().isEmpty()) {
        predicate = cb.and(predicate, cb.like(cb.lower(root.get("fullName")),
            "%" + filters.get("name").toString().toLowerCase() + "%"));
      }

      if (filters.containsKey("email") && filters.get("email") != null && !filters.get("email").toString().isEmpty()) {
        predicate = cb.and(predicate, cb.equal(root.get("email"), filters.get("email").toString()));
      }

      // BUG-2 FIX: Role is an Enum — cb.like() causes ClassCastException. Use cb.equal() with enum parsing.
      if (filters.containsKey("role") && filters.get("role") != null && !filters.get("role").toString().isEmpty()) {
        try {
          UserRole roleEnum = UserRole.valueOf(filters.get("role").toString().toUpperCase());
          predicate = cb.and(predicate, cb.equal(root.get("role"), roleEnum));
        } catch (IllegalArgumentException ignored) {
          // Invalid role value — skip filter to avoid crashing; will return unfiltered results
        }
      }

      if (filters.containsKey("fullName") && filters.get("fullName") != null
          && !filters.get("fullName").toString().isEmpty()) {
        predicate = cb.and(predicate, cb.like(cb.lower(root.get("fullName")),
            "%" + filters.get("fullName").toString().toLowerCase() + "%"));
      }

      if (filters.containsKey("phone") && filters.get("phone") != null && !filters.get("phone").toString().isEmpty()) {
        predicate = cb.and(predicate, cb.equal(root.get("phone"), filters.get("phone").toString()));
      }

      return predicate;
    };

    return userRepository.findAll(spec);
  }
}
