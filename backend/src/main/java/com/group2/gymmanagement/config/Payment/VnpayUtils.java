package com.group2.gymmanagement.config.Payment;


import jakarta.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VnpayUtils {

  VnpayConfig vnpayConfig;

  // Method sign use trong Service
  public String hmacSHA512(String value) {
    try {
      Mac hmac512 = Mac.getInstance("HmacSHA512");
      SecretKeySpec secretKeySpec = new SecretKeySpec(vnpayConfig.getHashSecret().getBytes(), "HmacSHA512");
      hmac512.init(secretKeySpec);
      return HexFormat.of().formatHex(hmac512.doFinal(value.getBytes(StandardCharsets.UTF_8)));
    } catch (Exception e) {
      throw new RuntimeException("Cannot create HMAC", e);
    }
  }

  // method build query theo chuan cua VNPAY
  public String buildQuery(Map<String, String> params) {
    return params.entrySet()
        .stream()
        .sorted(Map
            .Entry
            .comparingByKey())
        .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
        .collect(Collectors.joining("&"));
  }

  public String randomTxnRef() {
    return String.valueOf(new Random().nextInt(99999999));
  }

  public String getClientIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    if (ip != null  && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
      return ip.split(",")[0].trim();
    }

    ip = request.getHeader("X-Real-IP");
    if (ip != null  && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
      return ip;
    }

    return request.getRemoteAddr();
  }

}
