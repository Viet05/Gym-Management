package com.group2.gymmanagement.service;

import com.group2.gymmanagement.config.Payment.VnpayUtils;
import com.group2.gymmanagement.dto.request.PaymentRequest;
import com.group2.gymmanagement.dto.response.CreatePaymentResponse;
import com.group2.gymmanagement.dto.response.PaymentVerifyResult;
import com.group2.gymmanagement.entities.Payment;
import com.group2.gymmanagement.entities.Subscription;
import com.group2.gymmanagement.entities.User;
import com.group2.gymmanagement.enums.PackageStatus;
import com.group2.gymmanagement.enums.PaymentProvider;
import com.group2.gymmanagement.enums.PaymentStatus;
import com.group2.gymmanagement.repository.PaymentRepository;
import com.group2.gymmanagement.repository.SubscriptionRepository;
import com.group2.gymmanagement.service.payment.PaymentGateway;
import com.group2.gymmanagement.service.payment.PaymentGatewayFactory;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
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

  //Create Payment
  public CreatePaymentResponse createPayment(PaymentRequest paymentRequest,
      HttpServletRequest request,
      User currUser) {

    PaymentGateway gateway = gatewayFactory.getGateway(paymentRequest.getProvider());
    String clientIp = utils.getClientIp(request);
    Subscription subscription = findSubscription(paymentRequest.getSubscriptionId());

    Optional<Payment> existingPayment = paymentRepository
        .findBySubscriptionAndStatus(subscription, PaymentStatus.PENDING);

    if (!subscription.getMember().getUserId().equals(currUser.getUserId())) {
      throw new RuntimeException("Invalid subscription");
    }

    if (existingPayment.isPresent()) {
      Payment payment = existingPayment.get();

      String paymentUrl = gateway.createPaymentUrl(
          payment.getAmount(),
          payment.getReference(),
          clientIp
      );
      return buildResponse(payment, payment.getReference(), paymentUrl, gateway.getProviderName());
    }

    String txnRef = utils.randomTxnRef();
    Payment payment = createPendingPayment(subscription, txnRef);

    String paymentUrl = gateway.createPaymentUrl(payment.getAmount(), txnRef, clientIp);

    return buildResponse(payment, txnRef, paymentUrl, gateway.getProviderName());
  }

  //Callback
  public PaymentVerifyResult callBack(String provider, Map<String, String> params) {

    PaymentProvider paymentProvider = PaymentProvider.valueOf(provider.toUpperCase());
    PaymentGateway gateway = gatewayFactory.getGateway(paymentProvider);

    return gateway.verifyCallback(params);
  }

  //IPN
  public PaymentVerifyResult handleIpn(String provider, Map<String, String> params) {

    PaymentProvider paymentProvider = PaymentProvider.valueOf(provider.toUpperCase());
    PaymentGateway gateway = gatewayFactory.getGateway(paymentProvider);
    PaymentVerifyResult result = gateway.verifyCallback(params);

    if (result.isSuccess()) {
      throw new RuntimeException("Invalid IPN");
    }

    Payment payment = paymentRepository.findByReference(result.getTxnRef())
        .orElseThrow(() -> new RuntimeException("Invalid IPN"));

    if (payment.getStatus() != PaymentStatus.SUCCESS) {
      payment.setStatus(PaymentStatus.SUCCESS);
      paymentRepository.save(payment);

      Subscription subscription = payment.getSubscription();
      subscription.setStartDate(LocalDate.now());
      subscription.setEndDate(LocalDate.now().plusMonths(1));
      subscription.setStatus(PackageStatus.ACTIVE);
      subscriptionRepository.save(subscription);
    }

    return result;
  }

  private Subscription findSubscription(Long subscriptionId) {
    return subscriptionRepository.findSubscriptionBySubscriptionId(
        subscriptionId)
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
