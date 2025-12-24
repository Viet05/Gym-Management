package com.group2.gymmanagement.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {

  USER_EXISTED(1001, "User already existed"),
  USER_NOT_EXISTED(1002, "User does not exist");

  private final int code;

  private final String message;

  ErrorCode(int code, String message) {
    this.code = code;
    this.message = message;
  }

}
