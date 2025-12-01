package com.group2.gymmanagement.mapper;

import com.group2.gymmanagement.dto.response.MembershipPackageAssignmentDTO;
import com.group2.gymmanagement.entities.MemberPackageAssigment;
import com.group2.gymmanagement.entities.MembershipPackage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberPackageMapper {

  MembershipPackageAssignmentDTO toDTO(MemberPackageAssigment request);
}
