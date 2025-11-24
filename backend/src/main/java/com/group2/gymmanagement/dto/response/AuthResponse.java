package com.group2.gymmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {

  private String accessToken;
  private String tokenType = "Bearer";
  private Long userId;
  private String userName;
}
