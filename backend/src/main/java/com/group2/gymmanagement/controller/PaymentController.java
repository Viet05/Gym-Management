package com.group2.gymmanagement.controller;

import com.group2.gymmanagement.dto.request.PaymentRequest;
import com.group2.gymmanagement.dto.response.ApiResponse;
import com.group2.gymmanagement.dto.response.CreatePaymentResponse;
import com.group2.gymmanagement.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {

  PaymentService paymentService;

  @PostMapping("/create")
  public ApiResponse<CreatePaymentResponse> createPayment(
      @RequestBody PaymentRequest paymentRequest,
      HttpServletRequest request) {

    return ApiResponse.<CreatePaymentResponse>builder()
        .code(200)
        .message("Create payment successful")
        .data(paymentService.createPayment(paymentRequest, request))
        .build();
  }
}
