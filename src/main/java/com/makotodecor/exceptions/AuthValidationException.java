package com.makotodecor.exceptions;

import org.springframework.http.HttpStatus;

import com.makotodecor.exceptions.base.DetailException;
import com.makotodecor.exceptions.base.ErrorMessage;

public class AuthValidationException extends DetailException {

  public AuthValidationException(ErrorMessage errorMessage) {
    super(errorMessage, HttpStatus.BAD_REQUEST);
  }

  public AuthValidationException(String code, String message) {
    super(code, message, HttpStatus.BAD_REQUEST);
  }

  public static AuthValidationException withMultipleErrors() {
    return new AuthValidationException("A000", "Authentication validation failed");
  }
}
