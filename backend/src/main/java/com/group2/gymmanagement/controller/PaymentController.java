package com.group2.gymmanagement.controller;

import com.group2.gymmanagement.dto.request.PaymentRequest;
import com.group2.gymmanagement.dto.response.ApiResponse;
import com.group2.gymmanagement.dto.response.CreatePaymentResponse;
import com.group2.gymmanagement.dto.response.PaymentVerifyResult;
import com.group2.gymmanagement.entities.User;
import com.group2.gymmanagement.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {

  PaymentService paymentService;

  @PostMapping("/create")
  public ApiResponse<CreatePaymentResponse> createPayment(
      @RequestBody PaymentRequest paymentRequest,
      HttpServletRequest request,
      @AuthenticationPrincipal User user) {

    return ApiResponse.<CreatePaymentResponse>builder()
        .code(200)
        .message("Create payment successful")
        .data(paymentService.createPayment(
            paymentRequest,
            request,
            user))
        .build();
  }

  @GetMapping("/{provider}/return")
  public ApiResponse<PaymentVerifyResult> callBack(
      @PathVariable String provider,
      @RequestParam Map<String, String> params) {

    return ApiResponse.<PaymentVerifyResult>builder()
        .code(200)
        .message("Payment verification successful")
        .data(paymentService.callBack(provider, params))
        .build();
  }

  @PostMapping("/{provider}/ipn")
  public ApiResponse<String> callBackIpn(@PathVariable String provider,
      @RequestParam Map<String, String> params) {

    paymentService.handleIpn(provider, params);
    return ApiResponse.<String>builder()
        .code(200)
        .message("Succes to call IPN")
        .data("Payment IPN successful")
        .build();
  }
}
