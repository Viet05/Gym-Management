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

/**
 * REST controller for gym plan/package management.
 * Provides CRUD operations for gym plans.
 */
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class GymPlanController {

    GymPlanService planService;

    /**
     * Creates a new gym plan.
     */
    @PostMapping("/plans")
    public ApiResponse<PackageDTO> create(@RequestBody PackageCreateRequest request) {
        return ApiResponse.<PackageDTO>builder()
                .code(200)
                .message("Plan created successfully")
                .data(planService.create(request))
                .build();
    }

    /**
     * Updates an existing gym plan.
     */
    @PutMapping("/plans/{id}")
    public ApiResponse<PackageDTO> update(@PathVariable Long id, @RequestBody PackageUpdateRequest request) {
        return ApiResponse.<PackageDTO>builder()
                .code(200)
                .message("Plan updated successfully")
                .data(planService.update(id, request))
                .build();
    }

    /**
     * Retrieves all gym plans.
     */
    @GetMapping("/plans")
    public ApiResponse<List<PackageDTO>> getAll() {
        return ApiResponse.<List<PackageDTO>>builder()
                .code(200)
                .message("Success")
                .data(planService.getAll())
                .build();
    }

    /**
     * Deletes a gym plan by ID.
     */
    @DeleteMapping("/plans/{id}")
    public ApiResponse<String> delete(@PathVariable Long id) {
        planService.delete(id);
        return ApiResponse.<String>builder()
                .code(200)
                .message("Plan deleted successfully")
                .build();
    }
}
