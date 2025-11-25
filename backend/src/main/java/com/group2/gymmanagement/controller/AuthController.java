package com.group2.gymmanagement.controller;

import com.group2.gymmanagement.dto.request.LoginRequest;
import com.group2.gymmanagement.dto.request.RegisterRequest;
import com.group2.gymmanagement.dto.response.ApiResponse;
import com.group2.gymmanagement.dto.response.AuthResponse;
import com.group2.gymmanagement.service.AuthenService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

  AuthenService authen;

  @PostMapping(value = "/auth/register")
  public ApiResponse<AuthResponse> register(@RequestBody @Valid RegisterRequest request) {

    return ApiResponse.<AuthResponse>builder()
        .code(200)
        .message("Success")
        .data(authen.registerUser(request))
        .build();
  }

  @PostMapping(value = "/auth/login")
  public ApiResponse<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
    return ApiResponse.<AuthResponse>builder()
        .code(200)
        .message("Success")
        .data(authen.login(request))
        .build();
  }
}
