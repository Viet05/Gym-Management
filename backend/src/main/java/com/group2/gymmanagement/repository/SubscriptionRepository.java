package com.group2.gymmanagement.repository;

import com.group2.gymmanagement.entities.Subscription;
import com.group2.gymmanagement.entities.User;
import com.group2.gymmanagement.enums.PackageStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByMemberUserId(Long memberId);

    List<Subscription> findByStatus(PackageStatus status);

    java.util.Optional<Subscription> findByMemberAndGymPlanAndStatus(com.group2.gymmanagement.entities.User member,
            com.group2.gymmanagement.entities.GymPlan gymPlan, PackageStatus status);

    List<Subscription> findByMember(User member);

    Optional<Subscription> findSubscriptionBySubscriptionId(Long id);
}
