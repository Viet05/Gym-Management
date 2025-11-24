package com.group2.gymmanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.group2.gymmanagement.enums.UserRole;
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

  private String fullName;

  private String email;

  private String phone;

  private String status;

  private UserRole role;

  private String password;
}
