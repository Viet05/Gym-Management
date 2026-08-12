package com.group2.gymmanagement.controller;

import com.group2.gymmanagement.dto.request.AutoScheduleRequest;
import com.group2.gymmanagement.dto.response.ApiResponse;
import com.group2.gymmanagement.dto.response.TrainingSessionDTO;
import com.group2.gymmanagement.service.TrainingSessionService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/training-sessions")
@PreAuthorize("hasRole('ADMIN')")
public class TrainingSessionController {

  private final TrainingSessionService trainingSessionService;

  @PostMapping("/auto-assign")
  public ApiResponse<TrainingSessionDTO> autoAssign(@RequestBody @Valid AutoScheduleRequest request) {
    return ApiResponse.<TrainingSessionDTO>builder()
        .code(201)
        .message("Training session scheduled")
        .data(trainingSessionService.autoSchedule(request))
        .build();
  }

  @GetMapping("/trainer/{trainerId}")
  public ApiResponse<List<TrainingSessionDTO>> getTrainerSchedule(
      @PathVariable Long trainerId,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    return ApiResponse.<List<TrainingSessionDTO>>builder()
        .code(200)
        .message("Success")
        .data(trainingSessionService.getTrainerSchedule(trainerId, date))
        .build();
  }
}
