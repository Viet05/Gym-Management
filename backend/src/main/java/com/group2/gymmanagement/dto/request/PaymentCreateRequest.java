package com.group2.gymmanagement.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentCreateRequest {

  @NotNull(message = "Member ID is required")
  private Long memberId;

  @NotNull(message = "Package ID is required")
  private Long membershipPackageId;

  @NotNull(message = "Amount is required")
  @Positive(message = "Amount must be positive")
  private Double amount;

  /** Accepted values: CASH, BANK_TRANSFER */
  @NotNull(message = "Payment method is required")
  private String method;

  @Size(max = 500)
  private String description;

  /** Defaults to today if null. */
  private LocalDate paymentDate;
}
