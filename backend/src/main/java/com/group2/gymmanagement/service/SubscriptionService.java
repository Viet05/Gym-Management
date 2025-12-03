package com.group2.gymmanagement.service;

import com.group2.gymmanagement.dto.request.MemberPackageRegisterRequest;
import com.group2.gymmanagement.dto.response.MembershipPackageAssignmentDTO;
import com.group2.gymmanagement.entities.Subscription;
import com.group2.gymmanagement.entities.GymPlan;
import com.group2.gymmanagement.entities.User;
import com.group2.gymmanagement.enums.PackageStatus;
import com.group2.gymmanagement.mapper.MemberPackageMapper;
import com.group2.gymmanagement.repository.SubscriptionRepository;
import com.group2.gymmanagement.repository.GymPlanRepository;
import com.group2.gymmanagement.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

/**
 * Service for managing member subscriptions to gym plans.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubscriptionService {

        UserRepository userRepo;
        SubscriptionRepository subscriptionRepo;
        GymPlanRepository planRepo;
        MemberPackageMapper mapper;

        /**
         * Registers a member for a gym plan.
         */
        public MembershipPackageAssignmentDTO register(Long memberId, MemberPackageRegisterRequest request) {
                // Find member
                User member = userRepo.findById(memberId)
                                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));

                // Find plan
                GymPlan plan = planRepo.findById(request.getPackageId())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Plan not found: " + request.getPackageId()));

                // Calculate end date based on plan duration
                LocalDateTime endDate = request.getStartDate().plusMonths(plan.getDuration());

                // Create subscription
                Subscription subscription = Subscription.builder()
                                .member(member)
                                .gymPlan(plan)
                                .startDate(request.getStartDate().toLocalDate())
                                .endDate(endDate.toLocalDate())
                                .status(PackageStatus.PENDING)
                                .build();

                subscriptionRepo.save(subscription);
                return mapper.toDTO(subscription);
        }

        /**
         * Cancels an active subscription for a member.
         */
        public MembershipPackageAssignmentDTO cancel(Long memberId, Long planId) {
                // Find member
                User member = userRepo.findById(memberId)
                                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));

                // Find plan
                GymPlan plan = planRepo.findById(planId)
                                .orElseThrow(() -> new IllegalArgumentException("Plan not found: " + planId));

                // Find active subscription
                Subscription subscription = subscriptionRepo
                                .findByMemberAndGymPlanAndStatus(member, plan, PackageStatus.ACTIVE)
                                .orElseThrow(() -> new IllegalArgumentException("No active subscription found"));

                // Cancel subscription
                subscription.setStatus(PackageStatus.CANCEL);
                subscription.setEndDate(java.time.LocalDate.now());

                subscriptionRepo.save(subscription);
                return mapper.toDTO(subscription);
        }

        /**
         * Gets all active subscriptions for a member.
         */
        public List<MembershipPackageAssignmentDTO> getActive(Long memberId) {
                // Find member
                User member = userRepo.findById(memberId)
                                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));

                // Get all subscriptions
                List<Subscription> subscriptions = subscriptionRepo.findByMember(member);

                // Filter active subscriptions
                List<Subscription> activeSubscriptions = subscriptions.stream()
                                .filter(s -> s.getStatus() == PackageStatus.ACTIVE)
                                .toList();

                return mapper.toDTO_LIST_LIST(activeSubscriptions);
        }
}
