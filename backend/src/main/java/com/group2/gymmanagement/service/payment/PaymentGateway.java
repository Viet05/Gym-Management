package com.group2.gymmanagement.service.payment;

import com.group2.gymmanagement.dto.response.PaymentVerifyResult;
import com.group2.gymmanagement.enums.PaymentProvider;
import java.util.Map;


public interface PaymentGateway {

    String createPaymentUrl(Long amount, String txnRef, String clientIp);

//    PaymentVerifyResult verifyCallback(Map<String, String> params);

    String getProviderName();

    PaymentProvider getProvider();
}
