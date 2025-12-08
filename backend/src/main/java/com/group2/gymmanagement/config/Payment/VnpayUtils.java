package com.group2.gymmanagement.config.Payment;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VnpayUtils {

  private final VnpayConfig vnpayConfig;

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

  public String buildQuery(Map<String, String> params) {
    return params.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
        .collect(Collectors.joining("&"));
  }

  public String randomTxnRef() {
    return String.valueOf(new Random().nextInt(99999999));
  }
}
