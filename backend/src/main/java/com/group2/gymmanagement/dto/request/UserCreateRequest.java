package com.group2.gymmanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.group2.gymmanagement.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCreateRequest {

  @NotBlank
  @Size(min = 3, max = 20)
  private String userName;

  @NotBlank
  private String fullName;

  @NotBlank
  @Email
  private String email;

  @NotBlank
  private String phone;

  @NotBlank
  @Size(min = 8, max = 72)
  private String password;

  private UserRole role;     // MEMBER tự đăng ký → role=MEMBER
}

