package com.group2.gymmanagement.controller;

import com.group2.gymmanagement.dto.request.UserCreateRequest;
import com.group2.gymmanagement.dto.request.UserUpdateRequest;
import com.group2.gymmanagement.dto.response.ApiResponse;
import com.group2.gymmanagement.dto.response.DashboardStatsDTO;
import com.group2.gymmanagement.dto.response.UserDTO;
import com.group2.gymmanagement.enums.UserRole;
import com.group2.gymmanagement.repository.AttendanceRepository;
import com.group2.gymmanagement.repository.PaymentRepository;
import com.group2.gymmanagement.repository.UserRepository;
import com.group2.gymmanagement.service.UserService;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

  UserService userService;
  UserRepository userRepository;
  PaymentRepository paymentRepository;
  AttendanceRepository attendanceRepository;

  @PostMapping(value = "/users")
  public ApiResponse<UserDTO> createUser(@RequestBody @Valid UserCreateRequest request) {
    return ApiResponse.<UserDTO>builder()
        .code(200)
        .message("Success")
        .data(userService.createUser(request))
        .build();
  }

  @PutMapping(value = "/users/{id}")
  public ApiResponse<UserDTO> updateUser(@PathVariable("id") Long id, @RequestBody @Valid UserUpdateRequest request) {
    return ApiResponse.<UserDTO>builder()
        .code(200)
        .message("Success")
        .data(userService.updateUser(id, request))
        .build();
  }

  @GetMapping(value = "/users")
  public ApiResponse<List<UserDTO>> getAllUsers(@RequestParam Map<String, Object> request) {
    return ApiResponse.<List<UserDTO>>builder()
        .code(200)
        .message("Success")
        .data(userService.getUser(request))
        .build();
  }

  @GetMapping(value = "/stats")
  public ApiResponse<DashboardStatsDTO> getDashboardStats() {
    Double totalRevenue = paymentRepository.sumTotalRevenue();
    long totalMembers = userRepository.countByRole(UserRole.MEMBER);

    // BUG-6 FIX: count both "1" (legacy numeric) and "ACTIVE" (string) as active statuses
    long activeMembers = userRepository.countByStatusIn(Arrays.asList("1", "ACTIVE"));
    long visitsToday = attendanceRepository.countByDate(java.time.LocalDate.now().toString());

    DashboardStatsDTO stats = DashboardStatsDTO.builder()
        .totalRevenue(totalRevenue != null ? totalRevenue : 0.0)
        .totalMembers(totalMembers)
        .activeMembers(activeMembers)
        .visitsToday(visitsToday)
        .revenueTrend(12.5) // Mock trend — can be computed from payment history
        .memberTrend(8.2)   // Mock trend — can be computed from user join dates
        .build();

    return ApiResponse.<DashboardStatsDTO>builder()
        .code(200)
        .message("Success")
        .data(stats)
        .build();
  }

  /** BUG-4 FIX: Return ApiResponse<Void> instead of raw String for API contract consistency. */
  @DeleteMapping(value = "/users/{id}")
  public ApiResponse<Void> deleteUser(@PathVariable("id") Long id) {
    userService.deleteUser(id);
    return ApiResponse.<Void>builder()
        .code(200)
        .message("Deleted successfully")
        .build();
  }
}
