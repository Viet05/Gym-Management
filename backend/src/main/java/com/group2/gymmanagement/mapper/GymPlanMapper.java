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

/**
 * Mapper for converting between GymPlan entities and DTOs.
 */
@Mapper(componentModel = "spring")
public interface GymPlanMapper {

    // Convert create request to entity
    GymPlan toEntity(PackageCreateRequest request);

    // Convert entity to DTO
    PackageDTO toDTO(GymPlan plan);

    // Update existing entity from request
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    GymPlan update(PackageUpdateRequest request, @MappingTarget GymPlan target);

    // Convert list of entities to DTOs
    List<PackageDTO> toDTOList(List<GymPlan> plans);
}
