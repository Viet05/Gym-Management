package com.group2.gymmanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentDTO {

  private Long paymentId;
  private Long memberId;
  private String memberName;
  private Long membershipPackageId;
  private String packageName;
  private Double amount;
  private String method;
  private String reference;
  private String description;
  private String status;
  private LocalDate paymentDate;
  private LocalDateTime createdAt;
}
