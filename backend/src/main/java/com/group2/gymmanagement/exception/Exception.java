package com.group2.gymmanagement.exception;

import com.group2.gymmanagement.enums.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Exception extends RuntimeException {

  ErrorCode code;

  public Exception(ErrorCode code) {
    super(code.getMessage());
    this.code = code;
  }

}
