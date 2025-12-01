package com.group2.gymmanagement.service;


import com.group2.gymmanagement.dto.request.PackageCreateRequest;
import com.group2.gymmanagement.dto.request.PackageUpdateRequest;
import com.group2.gymmanagement.dto.response.PackageDTO;
import com.group2.gymmanagement.entities.MembershipPackage;
import com.group2.gymmanagement.enums.PackageStatus;
import com.group2.gymmanagement.mapper.PackageMapper;
import com.group2.gymmanagement.repository.MembershipPackageRepository;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PackageService {

  MembershipPackageRepository packageRepo;
  PackageMapper packageMapper;

  public PackageDTO createPackage(PackageCreateRequest request) {

    if (packageRepo.existsByName(request.getName())) {
      throw new RuntimeException("Package name already exists");
    }

    MembershipPackage pack = packageMapper.toMembershipPackage(request);
    pack.setActive(PackageStatus.ACTIVE);
    packageRepo.save(pack);

    return packageMapper.toPackageDTO(pack);
  }

  public PackageDTO updatePackage(Long id, PackageUpdateRequest request) {

    MembershipPackage packageUpdate = packageRepo.findById(id).orElseThrow(
        () -> new RuntimeException("Package not found")
    );

    MembershipPackage packages = packageMapper.toMembershipPackageUpdate(request, packageUpdate);
    packageRepo.save(packages);

    return packageMapper.toPackageDTO(packages);
  }

  public List<PackageDTO> getPackage() {
    List<MembershipPackage> packages = packageRepo.findAll();
    return packageMapper.toPackageDTOList(packages);
  }

  public void deletePackage(Long id) {
    packageRepo.deleteById(id);
  }
}
