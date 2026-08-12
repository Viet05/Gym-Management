package com.group2.gymmanagement.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.group2.gymmanagement.dto.request.LoginRequest;
import com.group2.gymmanagement.dto.request.RegisterRequest;
import com.group2.gymmanagement.entities.User;
import com.group2.gymmanagement.enums.UserRole;
import com.group2.gymmanagement.mapper.UserMapper;
import com.group2.gymmanagement.repository.UserRepository;
import com.group2.gymmanagement.security.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

class AuthenServiceTest {

  private final UserRepository userRepository = mock(UserRepository.class);
  private final UserMapper userMapper = mock(UserMapper.class);
  private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
  private final JwtUtils jwtUtils = mock(JwtUtils.class);
  private final AuthenService service = new AuthenService(userRepository, userMapper, passwordEncoder, jwtUtils);

  @Test
  void registerRejectsDuplicateEmailEvenWhenUsernameIsAvailable() {
    RegisterRequest request = RegisterRequest.builder().email("member@example.com").username("newmember").build();
    when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

    assertThatThrownBy(() -> service.registerUser(request))
        .isInstanceOf(ResponseStatusException.class)
        .extracting(error -> ((ResponseStatusException) error).getStatusCode())
        .isEqualTo(HttpStatus.CONFLICT);
  }

  @Test
  void loginRejectsInactiveAccountAfterSuccessfulPasswordCheck() {
    LoginRequest request = new LoginRequest("member", "password123");
    User user = User.builder().username("member").password("hash").role(UserRole.MEMBER).status("INACTIVE").build();
    when(userRepository.findByUsername("member")).thenReturn(java.util.Optional.of(user));
    when(passwordEncoder.matches("password123", "hash")).thenReturn(true);

    assertThatThrownBy(() -> service.login(request))
        .isInstanceOf(ResponseStatusException.class)
        .extracting(error -> ((ResponseStatusException) error).getStatusCode())
        .isEqualTo(HttpStatus.FORBIDDEN);
  }

  @Test
  void registerCreatesMemberWithEncodedPassword() {
    RegisterRequest request = RegisterRequest.builder()
        .email("member@example.com").username("member").password("password123").build();
    User user = User.builder().id(10L).username("member").build();
    when(userMapper.registerUser(request)).thenReturn(user);
    when(passwordEncoder.encode("password123")).thenReturn("encoded-password");
    when(jwtUtils.generateToken(user)).thenReturn("token");

    service.registerUser(request);

    verify(userRepository).save(user);
    verify(passwordEncoder).encode("password123");
    assertThat(user.getRole()).isEqualTo(UserRole.MEMBER);
    assertThat(user.getStatus()).isEqualTo("1");
    assertThat(user.getPassword()).isEqualTo("encoded-password");
  }
}
