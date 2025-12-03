package com.group2.gymmanagement.service;

import com.group2.gymmanagement.dto.request.PackageCreateRequest;
import com.group2.gymmanagement.dto.request.PackageUpdateRequest;
import com.group2.gymmanagement.dto.response.PackageDTO;
import com.group2.gymmanagement.entities.GymPlan;
import com.group2.gymmanagement.enums.PackageStatus;
import com.group2.gymmanagement.mapper.PackageMapper;
import com.group2.gymmanagement.repository.GymPlanRepository;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

/**
 * Service for managing gym plans/packages.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GymPlanService {

    GymPlanRepository planRepo;
    PackageMapper mapper;

    /**
     * Creates a new gym plan.
     */
    public PackageDTO create(PackageCreateRequest request) {
        if (planRepo.existsByName(request.getName())) {
            throw new RuntimeException("Plan name already exists");
        }

        GymPlan plan = mapper.toMembershipPackage(request);
        plan.setStatus(1);
        planRepo.save(plan);

        return mapper.toPackageDTO(plan);
    }

    /**
     * Updates an existing gym plan.
     */
    public PackageDTO update(Long id, PackageUpdateRequest request) {
        GymPlan plan = planRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        GymPlan updated = mapper.toMembershipPackageUpdate(request, plan);
        // Ensure status is preserved or handled if needed
        planRepo.save(updated);

        return mapper.toPackageDTO(updated);
    }

    /**
     * Retrieves all gym plans.
     */
    public List<PackageDTO> getAll() {
        List<GymPlan> plans = planRepo.findAll();
        return mapper.toPackageDTOList(plans);
    }

    /**
     * Deletes a gym plan by ID.
     */
    public void delete(Long id) {
        planRepo.deleteById(id);
    }
}
