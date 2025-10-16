package com.makotodecor.exceptions.base;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public abstract class DetailException extends RuntimeException {

  private final HttpStatus httpStatus;
  private final String message;
  private final String code;
  private final List<ErrorField> details = new ArrayList<>();

  protected DetailException(ErrorMessage error) {
    this(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  protected DetailException(ErrorMessage error, HttpStatus httpStatus) {
    this.code = error.getCode();
    this.message = error.getMessage();
    this.httpStatus = httpStatus;
  }

  protected DetailException(String code, String message, HttpStatus httpStatus) {
    this.code = code;
    this.message = message;
    this.httpStatus = httpStatus;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public String getCode() {
    return code;
  }

  public List<ErrorField> getDetails() {
    return details;
  }

  public void addError(String target, ErrorMessage errorMessage) {
    details.add(new ErrorField(errorMessage.getCode(), errorMessage.getMessage(), target));
  }

  public boolean hasErrors() {
    return !details.isEmpty();
  }
}
