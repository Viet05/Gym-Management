package com.group2.gymmanagement.mapper;

import com.group2.gymmanagement.dto.response.CreatePaymentResponse;
import com.group2.gymmanagement.entities.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

  CreatePaymentResponse toCreatePaymentResponse(Payment payment);
}
