package com.group2.gymmanagement.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainingSessionDTO {
  private Long id;
  private Long trainerId;
  private String trainerName;
  private Long memberId;
  private String memberName;
  private LocalDate sessionDate;
  private LocalTime startTime;
  private LocalTime endTime;
  private String notes;
  private String status;
}
