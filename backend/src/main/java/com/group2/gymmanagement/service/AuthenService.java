package com.group2.gymmanagement.service;

import com.group2.gymmanagement.dto.request.LoginRequest;
import com.group2.gymmanagement.dto.request.RegisterRequest;
import com.group2.gymmanagement.dto.response.AuthResponse;
import com.group2.gymmanagement.entities.User;
import com.group2.gymmanagement.enums.UserRole;
import com.group2.gymmanagement.mapper.UserMapper;
import com.group2.gymmanagement.repository.UserRepository;
import com.group2.gymmanagement.security.JwtUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenService {

  UserRepository userRepository;
  UserMapper userMapper;
  PasswordEncoder passwordEncoder;
  JwtUtils jwtUtils;

  public AuthResponse registerUser(RegisterRequest request) {

    if (userRepository.existsByEmail(request.getEmail()) && userRepository.existsByUserName(
        request.getUsername())) {
      throw new RuntimeException("Email already in use");
    }

    User user = userMapper.registerUser(request);
    user.setRole(UserRole.MEMBER);
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setStatus("1");

    userRepository.save(user);

    String token = jwtUtils.generateToken(user);

    return new AuthResponse(
        token,
        "Bearer",
        user.getId(),
        user.getUserName()
    );
  }

  public AuthResponse login(LoginRequest request){

    var user = userRepository.findByUserName(request.getUsername()).orElseThrow(
        () -> new RuntimeException("Incorrect username or password")
    );

    boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword());

    if (!matches) {
      throw new RuntimeException("Incorrect username or password");
    }

    String token = jwtUtils.generateToken(user);

    return new AuthResponse(
        token,
        "Bearer",
        user.getId(),
        user.getUserName()
    );
  }
}
