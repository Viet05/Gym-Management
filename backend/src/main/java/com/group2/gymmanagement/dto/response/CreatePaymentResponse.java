package com.group2.gymmanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreatePaymentResponse {

  private String paymentUrl;
  private String paymentReference;
  private Long amount;
  private String currency;
  private Long subscriptionId;
  private String provider;
}
