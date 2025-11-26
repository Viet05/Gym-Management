package com.group2.gymmanagement.mapper;

import com.group2.gymmanagement.dto.request.PackageCreateRequest;
import com.group2.gymmanagement.dto.response.PackageDTO;
import com.group2.gymmanagement.entities.MembershipPackage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PackageMapper {

  MembershipPackage toMembershipPackage(PackageCreateRequest request);

  PackageDTO toPackageDTO(MembershipPackage request);
}
