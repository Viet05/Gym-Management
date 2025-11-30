package com.group2.gymmanagement.mapper;

import com.group2.gymmanagement.dto.request.PackageCreateRequest;
import com.group2.gymmanagement.dto.request.PackageUpdateRequest;
import com.group2.gymmanagement.dto.response.PackageDTO;
import com.group2.gymmanagement.entities.MembershipPackage;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PackageMapper {

  MembershipPackage toMembershipPackage(PackageCreateRequest request);

  PackageDTO toPackageDTO(MembershipPackage request);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "id", ignore = true)
  MembershipPackage toMembershipPackageUpdate(PackageUpdateRequest request, @MappingTarget MembershipPackage target);

  List<PackageDTO> toPackageDTOList(List<MembershipPackage> membershipPackages);
}
