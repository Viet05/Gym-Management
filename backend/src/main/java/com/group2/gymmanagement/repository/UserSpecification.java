package com.group2.gymmanagement.repository;

import com.group2.gymmanagement.dto.UserDTO;
import com.group2.gymmanagement.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSpecification {

  @PersistenceContext
  EntityManager em;

  public List<User> getUserSpec(Map<String, Objects> request) {

    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<User> query = cb.createQuery(User.class);
    Root<User> root = query.from(User.class);

    List<Predicate> predicates = new ArrayList<>();

    if (request.containsKey("fullName")
        &&request.get("fullName") != null) {
        predicates.add(cb.like(root.get("fullName"), "%" + request.get("fullName") + "%"));
    }

    if (request.containsKey("email")
    &&request.get("email") != null) {
      predicates.add(cb.like(root.get("email"), "%" + request.get("email") + "%"));
    }

    if (!predicates.isEmpty()) {
      query.where(predicates.toArray(new Predicate[0]));
    }

    return em.createQuery(query).getResultList();
  }
}
