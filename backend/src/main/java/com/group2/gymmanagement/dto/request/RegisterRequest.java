package com.group2.gymmanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterRequest {

  @NotBlank(message = "Username is required")
  @Size(min = 8, max = 20)
  private String username;

  @Email(message = "Invalid email format")
  @NotBlank(message = "Email is required")
  private String email;

  @NotBlank
  private String fullName;

  @Size(min = 8, max = 20, message = "Password must be least 8 characters")
  @NotBlank
  private String password;

  @NotBlank(message = "Phone is required")
  private String phoneNumber;
}
