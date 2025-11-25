package com.group2.gymmanagement.service;


import com.group2.gymmanagement.dto.request.PackageCreateRequest;
import com.group2.gymmanagement.dto.response.PackageDTO;
import com.group2.gymmanagement.entities.MembershipPackage;
import com.group2.gymmanagement.mapper.PackageMapper;
import com.group2.gymmanagement.repository.MembershipPackageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PackageService {

  MembershipPackageRepository memPackageRepo;
  PackageMapper packageMapper;

  public PackageDTO createPackage(PackageCreateRequest request) {

    if (memPackageRepo.existsByName(request.getName())) {
      throw new RuntimeException("Package name already exists");
    }

    MembershipPackage pack = packageMapper.toMembershipPackage(request);
    memPackageRepo.save(pack);

    return packageMapper.toPackageDTO(pack);
  }

}
