package com.group2.gymmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentVerifyResult {

    private boolean success;
    private String txnRef;
    private String transactionNo;
    private Long amount;
    private String bankCode;
    private String responseCode;
    private String message;
}
