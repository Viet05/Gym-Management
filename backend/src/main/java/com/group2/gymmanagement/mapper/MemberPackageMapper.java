package com.group2.gymmanagement.mapper;

import com.group2.gymmanagement.dto.response.MembershipPackageAssignmentDTO;
import com.group2.gymmanagement.entities.Subscription;
import com.group2.gymmanagement.entities.GymPlan;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper for Subscription entity.
 */
@Mapper(componentModel = "spring")
public interface MemberPackageMapper {

  // Convert subscription to DTO
  @org.mapstruct.Mapping(source = "member.id", target = "memberId")
  @org.mapstruct.Mapping(source = "gymPlan.id", target = "packageId")
  MembershipPackageAssignmentDTO toDTO(Subscription request);

  // Convert list of plans to DTOs
  List<MembershipPackageAssignmentDTO> toDTO_LIST(List<GymPlan> request);

  // Convert list of subscriptions to DTOs
  List<MembershipPackageAssignmentDTO> toDTO_LIST_LIST(List<Subscription> request);
}
