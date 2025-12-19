package com.group2.gymmanagement.mapper;

import com.group2.gymmanagement.dto.request.PackageCreateRequest;
import com.group2.gymmanagement.dto.request.PackageUpdateRequest;
import com.group2.gymmanagement.dto.response.PackageDTO;
import com.group2.gymmanagement.entities.GymPlan;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PackageMapper {

  // Convert request to entity
  @Mapping(source = "durationMonth", target = "duration")
  GymPlan toMembershipPackage(PackageCreateRequest request);

  // Convert entity to DTO
  @Mapping(source = "duration", target = "durationMonth")
  @Mapping(target = "active", expression = "java(request.getStatus() == 1 ? \"Active\" : \"Inactive\")")
  PackageDTO toPackageDTO(GymPlan request);

  // Update entity from request
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "gymPlanId", ignore = true)
  @Mapping(source = "durationMonth", target = "duration")
  GymPlan toMembershipPackageUpdate(PackageUpdateRequest request, @MappingTarget GymPlan target);

  // Convert list of entities to DTOs
  List<PackageDTO> toPackageDTOList(List<GymPlan> membershipPackages);
}
