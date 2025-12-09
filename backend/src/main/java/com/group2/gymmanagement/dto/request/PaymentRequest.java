package com.group2.gymmanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.group2.gymmanagement.enums.PaymentProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentRequest {

    private Long subscriptionId;

    private String locale = "vn";

    @Builder.Default
    private PaymentProvider provider = PaymentProvider.VNPAY;
}
