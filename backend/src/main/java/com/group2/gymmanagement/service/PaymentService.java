package com.group2.gymmanagement.service;

import com.group2.gymmanagement.dto.request.PaymentCreateRequest;
import com.group2.gymmanagement.dto.request.PaymentUpdateRequest;
import com.group2.gymmanagement.dto.response.PaymentDTO;
import com.group2.gymmanagement.entities.MembershipPackage;
import com.group2.gymmanagement.entities.Payment;
import com.group2.gymmanagement.entities.Payment.PaymentStatus;
import com.group2.gymmanagement.entities.User;
import com.group2.gymmanagement.repository.MembershipPackageRepository;
import com.group2.gymmanagement.repository.PaymentRepository;
import com.group2.gymmanagement.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PaymentService {

  private final PaymentRepository paymentRepository;
  private final UserRepository userRepository;
  private final MembershipPackageRepository packageRepository;

  @Transactional
  public PaymentDTO createPayment(PaymentCreateRequest request) {
    User member = userRepository.findById(request.getMemberId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));

    MembershipPackage pkg = packageRepository.findById(request.getMembershipPackageId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Membership package not found"));

    Payment payment = Payment.builder()
        .member(member)
        .membershipPackage(pkg)
        .amount(request.getAmount())
        .method(request.getMethod())
        .description(request.getDescription())
        .paymentDate(request.getPaymentDate() != null ? request.getPaymentDate() : LocalDate.now())
        .status(PaymentStatus.COMPLETED)
        .build();

    return toDto(paymentRepository.save(payment));
  }

  @Transactional
  public PaymentDTO updatePayment(Long id, PaymentUpdateRequest request) {
    Payment payment = paymentRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));

    if (request.getStatus() != null) {
      try {
        payment.setStatus(PaymentStatus.valueOf(request.getStatus().toUpperCase()));
      } catch (IllegalArgumentException e) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "Invalid status. Accepted values: PENDING, COMPLETED, CANCELLED");
      }
    }

    if (request.getDescription() != null) {
      payment.setDescription(request.getDescription());
    }

    return toDto(paymentRepository.save(payment));
  }

  @Transactional(readOnly = true)
  public Page<PaymentDTO> getAllPayments(Pageable pageable) {
    return paymentRepository.findAllByOrderByCreatedAtDesc(pageable).map(this::toDto);
  }

  @Transactional(readOnly = true)
  public List<PaymentDTO> getPaymentsByMember(Long memberId) {
    User member = userRepository.findById(memberId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));
    return paymentRepository.findByMemberOrderByCreatedAtDesc(member).stream()
        .map(this::toDto)
        .toList();
  }

  @Transactional
  public void deletePayment(Long id) {
    if (!paymentRepository.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found");
    }
    paymentRepository.deleteById(id);
  }

  private PaymentDTO toDto(Payment payment) {
    return PaymentDTO.builder()
        .paymentId(payment.getPaymentId())
        .memberId(payment.getMember() != null ? payment.getMember().getId() : null)
        .memberName(payment.getMember() != null ? payment.getMember().getFullName() : null)
        .membershipPackageId(payment.getMembershipPackage() != null ? payment.getMembershipPackage().getId() : null)
        .packageName(payment.getMembershipPackage() != null ? payment.getMembershipPackage().getName() : null)
        .amount(payment.getAmount())
        .method(payment.getMethod())
        .reference(payment.getReference())
        .description(payment.getDescription())
        .status(payment.getStatus() != null ? payment.getStatus().name() : null)
        .paymentDate(payment.getPaymentDate())
        .createdAt(payment.getCreatedAt())
        .build();
  }
}
