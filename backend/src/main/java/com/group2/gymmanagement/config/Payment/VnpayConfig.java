package com.group2.gymmanagement.config.Payment;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
//Annotation take variable from .env
@ConfigurationProperties(prefix = "vnpay")
@Data
public class VnpayConfig {

  private String url;
  private String returnUrl;
  private String ipnUrl;
  private String tmnCode;
  private String hashSecret;
  private String version;
  private String command;
  private String currCode;
  private String locale;
  private Integer expireMinutes;
  private String timeZone;
}
