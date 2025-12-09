package com.group2.gymmanagement.service.payment;

import com.group2.gymmanagement.config.Payment.VnpayConfig;
import com.group2.gymmanagement.config.Payment.VnpayUtils;
import com.group2.gymmanagement.dto.response.PaymentVerifyResult;
import com.group2.gymmanagement.enums.PaymentProvider;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VnpayService implements PaymentGateway {

    VnpayConfig config;
    VnpayUtils utils;

    @Override
    public PaymentProvider getProvider() {
        return PaymentProvider.VNPAY;
    }

    @Override
    public String getProviderName() {
        return "VNPAY";
    }

    @Override
    public String createPaymentUrl(Long amount, String txnRef, String clientIp) {
        Map<String, String> params = buildParams(amount, txnRef, clientIp);
        String query = utils.buildQuery(params);
        String secureHash = utils.hmacSHA512(query);
        return config.getUrl() + "?" + query + "&vnp_SecureHash=" + secureHash;
    }

//    @Override
//    public PaymentVerifyResult verifyCallback(Map<String, String> params) {
//        String vnpSecureHash = params.get("vnp_SecureHash");
//
//
//        Map<String, String> paramsToVerify = new LinkedHashMap<>(params);
//        paramsToVerify.remove("vnp_SecureHash");
//        paramsToVerify.remove("vnp_SecureHashType");
//
//        // Build query and compute hash
//        String query = utils.buildQuery(paramsToVerify);
//        String computedHash = utils.hmacSHA512(query);
//
//        boolean isValidSignature = computedHash.equalsIgnoreCase(vnpSecureHash);
//        String responseCode = params.get("vnp_ResponseCode");
//        boolean isSuccess = isValidSignature && "00".equals(responseCode);
//
//        return PaymentVerifyResult.builder()
//                .success(isSuccess)
//                .txnRef(params.get("vnp_TxnRef"))
//                .transactionNo(params.get("vnp_TransactionNo"))
//                .amount(parseLong(params.get("vnp_Amount")))
//                .bankCode(params.get("vnp_BankCode"))
//                .responseCode(responseCode)
//                .message(isSuccess ? "Payment successful" : "Payment failed or invalid signature")
//                .build();
//    }

    private Map<String, String> buildParams(Long amount, String txnRef, String clientIp) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("vnp_Version", config.getVersion());
        params.put("vnp_Command", config.getCommand());
        params.put("vnp_TmnCode", config.getTmnCode());
        params.put("vnp_Amount", String.valueOf(amount));
        params.put("vnp_CurrCode", config.getCurrCode());
        params.put("vnp_Locale", config.getLocale());
        params.put("vnp_TxnRef", txnRef);
        params.put("vnp_OrderInfo", "Titanium Payment #" + txnRef);
        params.put("vnp_ReturnUrl", config.getReturnUrl());
        params.put("vnp_IpAddr", clientIp);
        return params;
    }

    private Long parseLong(String value) {
        try {
            return value != null ? Long.parseLong(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
