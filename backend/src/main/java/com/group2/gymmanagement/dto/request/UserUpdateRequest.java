package com.group2.gymmanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.group2.gymmanagement.enums.UserRole;
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
public class UserUpdateRequest {

  @Size(min = 3, max = 20)
  private String userName;

  private String fullName;

  private String email;

  private String phone;

  private String status;

  private UserRole role;

  private String password;
}
