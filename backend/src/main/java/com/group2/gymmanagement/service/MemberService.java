package com.group2.gymmanagement.service;

import com.group2.gymmanagement.dto.request.MemberPackageRegisterRequest;
import com.group2.gymmanagement.dto.response.MembershipPackageAssignmentDTO;
import com.group2.gymmanagement.entities.MemberPackageAssigment;
import com.group2.gymmanagement.entities.MembershipPackage;
import com.group2.gymmanagement.entities.User;
import com.group2.gymmanagement.enums.PackageStatus;
import com.group2.gymmanagement.mapper.MemberPackageMapper;
import com.group2.gymmanagement.repository.MemberPackageAssignmentRepository;
import com.group2.gymmanagement.repository.MembershipPackageRepository;
import com.group2.gymmanagement.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MemberService {

  UserRepository userRepository;
  MemberPackageAssignmentRepository assignmentRepository;
  MembershipPackageRepository packageRepository;
  MemberPackageMapper mapper;

  public MembershipPackageAssignmentDTO registerPackage(Long memberId, MemberPackageRegisterRequest request) {

    User member = userRepository.findById(memberId).orElseThrow(
        () -> new IllegalArgumentException("Invalid member id: " + memberId)
    );

    MembershipPackage packages = packageRepository.findById(request.getPackageId()).orElseThrow(
        () -> new IllegalArgumentException("Invalid package id: " + request.getPackageId())
    );

    LocalDateTime endDate = request.getStartDate().plusMonths(packages.getDurationMonth());

    MemberPackageAssigment assignment = MemberPackageAssigment.builder()
        .member(member)
        .membershipPackage(packages)
        .startDate(request.getStartDate())
        .endDate(endDate)
        .activeStatus(PackageStatus.ACTIVE)
        .build();
    assignmentRepository.save(assignment);

    return mapper.toDTO(assignment);
  }
}
