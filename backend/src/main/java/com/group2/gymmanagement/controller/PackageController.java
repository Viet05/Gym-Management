package com.group2.gymmanagement.controller;

import com.group2.gymmanagement.dto.request.PackageCreateRequest;
import com.group2.gymmanagement.dto.request.PackageUpdateRequest;
import com.group2.gymmanagement.dto.response.ApiResponse;
import com.group2.gymmanagement.dto.response.PackageDTO;
import com.group2.gymmanagement.service.PackageService;
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
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class PackageController {

  PackageService packageService;

  @PostMapping("/pakages")
  public ApiResponse<PackageDTO> createPackage(@RequestBody @Valid PackageCreateRequest request) {
    return ApiResponse.<PackageDTO>builder()
        .code(200)
        .message("Create success")
        .data(packageService.createPackage(request))
        .build();
  }

  @PutMapping("/pakages/{id}")
  public ApiResponse<PackageDTO> updatePackage(@PathVariable Long id, @RequestBody @Valid PackageUpdateRequest request) {
    return ApiResponse.<PackageDTO>builder()
        .code(200)
        .message("Update success")
        .data(packageService.updatePackage(id, request))
        .build();
  }

  @GetMapping("/pakages")
  public ApiResponse<List<PackageDTO>> getAllPackages() {
    return ApiResponse.<List<PackageDTO>>builder()
        .code(200)
        .message("Success")
        .data(packageService.getPackage())
        .build();
  }

  @DeleteMapping("/pakages/{id}")
  public String deletePackage(@PathVariable Long id) {
    packageService.deletePackage(id);
    return "Delete success";
  }
}
