package com.group2.gymmanagement.exception;

import com.group2.gymmanagement.dto.response.ApiResponse;
import com.group2.gymmanagement.enums.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = Exception.class)
  ResponseEntity<ApiResponse> handleAppException(final Exception e) {

    // Lấy ErrorCode đã được set trong AppException
    ErrorCode errorCode = e.getCode();

    // Tạo response trả về
    ApiResponse apiResponse = new ApiResponse();
    apiResponse.setCode(errorCode.getCode());     // set code lỗi
    apiResponse.setMessage(errorCode.getMessage()); // set message lỗi

    // Trả về response HTTP status 400 (Bad Request)
    return ResponseEntity
        .badRequest()
        .body(apiResponse);
  }


}
