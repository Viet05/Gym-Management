package com.group2.gymmanagement.service;

import com.group2.gymmanagement.config.Payment.VnpayUtils;
import com.group2.gymmanagement.dto.request.PaymentRequest;
import com.group2.gymmanagement.dto.response.CreatePaymentResponse;
import com.group2.gymmanagement.entities.Payment;
import com.group2.gymmanagement.entities.Subscription;
import com.group2.gymmanagement.enums.PaymentStatus;
import com.group2.gymmanagement.repository.PaymentRepository;
import com.group2.gymmanagement.repository.SubscriptionRepository;
import com.group2.gymmanagement.service.payment.PaymentGateway;
import com.group2.gymmanagement.service.payment.PaymentGatewayFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class PaymentService {

  PaymentGatewayFactory gatewayFactory;
  PaymentRepository paymentRepository;
  SubscriptionRepository subscriptionRepository;
  VnpayUtils utils;

  public CreatePaymentResponse createPayment(PaymentRequest paymentRequest, HttpServletRequest request) {
    PaymentGateway gateway = gatewayFactory.getGateway(paymentRequest.getProvider());

    Subscription subscription = findSubscription(paymentRequest.getSubscriptionId());

    String txnRef = utils.randomTxnRef();
    Payment payment = createPendingPayment(subscription, txnRef);

    String clientIp = utils.getClientIp(request);
    String paymentUrl = gateway.createPaymentUrl(payment.getAmount(), txnRef, clientIp);

    return buildResponse(payment, txnRef, paymentUrl, gateway.getProviderName());
  }

  private Subscription findSubscription(Long subscriptionId) {
    return subscriptionRepository.findSubscriptionById(subscriptionId)
        .orElseThrow(() -> new RuntimeException("Subscription not found with id: " + subscriptionId));
  }

  private Payment createPendingPayment(Subscription subscription, String txnRef) {
    Payment payment = Payment.builder()
        .subscription(subscription)
        .member(subscription.getMember())
        .amount(subscription.getGymPlan().getPrice())
        .reference(txnRef)
        .status(PaymentStatus.PENDING)
        .build();
    return paymentRepository.save(payment);
  }

  private CreatePaymentResponse buildResponse(Payment payment, String txnRef, String paymentUrl, String providerName) {
    return CreatePaymentResponse.builder()
        .paymentUrl(paymentUrl)
        .paymentReference(txnRef)
        .amount(payment.getAmount())
        .currency("VND")
        .provider(providerName)
        .build();
  }
}
