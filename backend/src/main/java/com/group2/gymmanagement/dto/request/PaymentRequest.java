package com.group2.gymmanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.group2.gymmanagement.enums.PaymentProvider;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Subscription not null")
    private Long subscriptionId;

    private String locale = "vn";

    @Builder.Default
    @NotNull(message = "Provider is required")
    private PaymentProvider provider = PaymentProvider.VNPAY;
}
