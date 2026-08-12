package com.group2.gymmanagement.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentUpdateRequest {

  /** Accepted values: PENDING, COMPLETED, CANCELLED */
  private String status;

  @Size(max = 500)
  private String description;
}
