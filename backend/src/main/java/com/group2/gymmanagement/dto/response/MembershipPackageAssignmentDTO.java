package com.group2.gymmanagement.dto.response;

import com.group2.gymmanagement.enums.PackageStatus;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MembershipPackageAssignmentDTO {

  Long id;
  Long memberId;
  Long packageId;
  LocalDateTime startDate;
  LocalDateTime endDate;
  PackageStatus status;
}
