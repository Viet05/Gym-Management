package com.group2.gymmanagement.controller;

import com.group2.gymmanagement.dto.request.PaymentCreateRequest;
import com.group2.gymmanagement.dto.request.PaymentUpdateRequest;
import com.group2.gymmanagement.dto.response.ApiResponse;
import com.group2.gymmanagement.dto.response.PaymentDTO;
import com.group2.gymmanagement.service.PaymentService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/payments")
@PreAuthorize("hasRole('ADMIN')")
public class PaymentController {

  private final PaymentService paymentService;

  /**
   * Create a new payment record.
   * POST /admin/payments
   */
  @PostMapping
  public ApiResponse<PaymentDTO> createPayment(@RequestBody @Valid PaymentCreateRequest request) {
    return ApiResponse.<PaymentDTO>builder()
        .code(201)
        .message("Payment created successfully")
        .data(paymentService.createPayment(request))
        .build();
  }

  /**
   * Update payment status or description.
   * PUT /admin/payments/{id}
   */
  @PutMapping("/{id}")
  public ApiResponse<PaymentDTO> updatePayment(
      @PathVariable Long id,
      @RequestBody @Valid PaymentUpdateRequest request) {
    return ApiResponse.<PaymentDTO>builder()
        .code(200)
        .message("Payment updated successfully")
        .data(paymentService.updatePayment(id, request))
        .build();
  }

  /**
   * Get paginated list of all payments (newest first).
   * GET /admin/payments?page=0&size=20
   */
  @GetMapping
  public ApiResponse<Page<PaymentDTO>> getAllPayments(
      @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
    return ApiResponse.<Page<PaymentDTO>>builder()
        .code(200)
        .message("Success")
        .data(paymentService.getAllPayments(pageable))
        .build();
  }

  /**
   * Get all payments for a specific member.
   * GET /admin/payments/member/{memberId}
   */
  @GetMapping("/member/{memberId}")
  public ApiResponse<List<PaymentDTO>> getPaymentsByMember(@PathVariable Long memberId) {
    return ApiResponse.<List<PaymentDTO>>builder()
        .code(200)
        .message("Success")
        .data(paymentService.getPaymentsByMember(memberId))
        .build();
  }

  /**
   * Delete a payment record.
   * DELETE /admin/payments/{id}
   */
  @DeleteMapping("/{id}")
  public ApiResponse<Void> deletePayment(@PathVariable Long id) {
    paymentService.deletePayment(id);
    return ApiResponse.<Void>builder()
        .code(200)
        .message("Payment deleted successfully")
        .build();
  }
}
