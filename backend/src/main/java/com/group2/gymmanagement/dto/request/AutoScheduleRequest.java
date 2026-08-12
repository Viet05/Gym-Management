package com.group2.gymmanagement.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;

@Data
public class AutoScheduleRequest {
  @NotNull private Long memberId;
  @NotNull @FutureOrPresent private LocalDate sessionDate;
  @NotNull private LocalTime startTime;
  @NotNull private LocalTime endTime;
  private String notes;
}
