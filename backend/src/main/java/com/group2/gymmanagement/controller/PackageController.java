package com.group2.gymmanagement.controller;

import com.group2.gymmanagement.dto.request.PackageCreateRequest;
import com.group2.gymmanagement.dto.request.PackageUpdateRequest;
import com.group2.gymmanagement.dto.response.ApiResponse;
import com.group2.gymmanagement.dto.response.PackageDTO;
import com.group2.gymmanagement.service.GymPlanService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class PackageController {

  GymPlanService planService;

  @PostMapping("/pakages")
  public ApiResponse<PackageDTO> createPackage(@RequestBody PackageCreateRequest request) {
    return ApiResponse.<PackageDTO>builder()
        .code(200)
        .message("Create success")
        .data(planService.create(request))
        .build();
  }

  @PutMapping("/pakages/{id}")
  public ApiResponse<PackageDTO> updatePackage(@PathVariable Long id, @RequestBody PackageUpdateRequest request) {
    return ApiResponse.<PackageDTO>builder()
        .code(200)
        .message("Update success")
        .data(planService.update(id, request))
        .build();
  }

  @GetMapping("/pakages")
  public ApiResponse<List<PackageDTO>> getAllPackages() {
    return ApiResponse.<List<PackageDTO>>builder()
        .code(200)
        .message("Success")
        .data(planService.getAll())
        .build();
  }

  @DeleteMapping("/pakages/{id}")
  public String deletePackage(@PathVariable Long id) {
    planService.delete(id);
    return "Delete success";
  }
}
