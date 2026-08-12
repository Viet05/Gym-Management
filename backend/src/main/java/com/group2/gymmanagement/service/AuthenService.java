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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
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

    if (userRepository.existsByEmail(request.getEmail())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
    }
    if (userRepository.existsByUsername(request.getUsername())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already in use");
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
        user.getUsername());
  }

  public AuthResponse login(LoginRequest request) {

    User user = userRepository.findByUsername(request.getUsername()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect username or password"));

    boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword());

    if (!matches) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect username or password");
    }

    if (!"1".equals(user.getStatus()) && !"ACTIVE".equalsIgnoreCase(user.getStatus())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account is inactive");
    }

    String token = jwtUtils.generateToken(user);

    return new AuthResponse(
        token,
        "Bearer",
        user.getId(),
        user.getUsername());
  }
}
