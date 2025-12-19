package com.group2.gymmanagement.service.payment;

import com.group2.gymmanagement.config.Payment.VnpayConfig;
import com.group2.gymmanagement.config.Payment.VnpayUtils;
import com.group2.gymmanagement.dto.response.PaymentVerifyResult;
import com.group2.gymmanagement.enums.PaymentProvider;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
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


    @Override
    public PaymentVerifyResult verifyCallback(Map<String, String> params) {
        String vnpSecureHash = params.get("vnp_SecureHash");

        Map<String, String> paramsMap = new TreeMap<>(params);
        paramsMap.remove("vnp_SecureHash");
        paramsMap.remove("vnp_SecureHashType");

        String query = utils.buildQuery(paramsMap);
        String secureHash = utils.hmacSHA512(query);

        boolean isValidSecureHash = secureHash.equals(vnpSecureHash);
        String responseCode = params.get("vnp_ResponseCode");
        boolean isSuccess = isValidSecureHash && "00".equals(responseCode);

        Long amount = 0L;
        try {
            if (params.get("vnp_Amount") != null) {
                amount = Long.parseLong(params.get("vnp_Amount")) / 100;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return PaymentVerifyResult.builder()
            .success(isSuccess)
            .txnRef(params.get("vnp_TxnRef"))
            .transactionNo(params.get("vnp_TransactionNo")) // có thể null
            .amount(amount)
            .bankCode(params.get("vnp_BankCode"))
            .responseCode(responseCode)
            .message(
                !isValidSecureHash ? "Invalid signature" :
                    "00".equals(responseCode) ? "Payment successful" :
                        "Payment failed with code: " + responseCode
            )
            .build();
    }

    private Map<String, String> buildParams(Long amount, String txnRef, String clientIp) {

        Map<String, String> params = new TreeMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", config.getTmnCode());
        params.put("vnp_Amount", String.valueOf(amount * 100));
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", txnRef);
        params.put("vnp_OrderInfo", "Payment for order #" + txnRef);


        params.put("vnp_OrderType", "other");

        params.put("vnp_Locale", "vn");
        params.put("vnp_IpAddr", clientIp);
        params.put("vnp_ReturnUrl", config.getReturnUrl());


        String createDate = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        params.put("vnp_CreateDate", createDate);


        String expireDate = LocalDateTime.now()
            .plusMinutes(15)
            .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        params.put("vnp_ExpireDate", expireDate);

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
