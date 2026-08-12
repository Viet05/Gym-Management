package com.group2.gymmanagement.exception;

import com.group2.gymmanagement.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException exception) {
    String message = exception.getBindingResult().getFieldErrors().stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.joining(", "));
    return response(HttpStatus.BAD_REQUEST, message);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException exception) {
    return response(HttpStatus.BAD_REQUEST, exception.getMessage());
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ApiResponse<Void>> handleResponseStatus(ResponseStatusException exception) {
    return response(HttpStatus.valueOf(exception.getStatusCode().value()), exception.getReason());
  }

  private ResponseEntity<ApiResponse<Void>> response(HttpStatus status, String message) {
    return ResponseEntity.status(status).body(ApiResponse.<Void>builder()
        .code(status.value())
        .message(message)
        .build());
  }
}
