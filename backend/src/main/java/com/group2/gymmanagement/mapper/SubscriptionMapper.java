package com.group2.gymmanagement.mapper;

import com.group2.gymmanagement.dto.response.MembershipPackageAssignmentDTO;
import com.group2.gymmanagement.entities.Subscription;
import com.group2.gymmanagement.entities.GymPlan;
import java.util.List;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    // Convert entity to DTO
    @org.mapstruct.Mapping(source = "member.userId", target = "memberId")
    @org.mapstruct.Mapping(source = "gymPlan.gymPlanId", target = "packageId")
    MembershipPackageAssignmentDTO toDTO(Subscription subscription);

    // Convert list of plans to DTOs
    List<MembershipPackageAssignmentDTO> planListToDTO(List<GymPlan> plans);

    // Convert list of subscriptions to DTOs
    List<MembershipPackageAssignmentDTO> toDTOList(List<Subscription> subscriptions);
}
