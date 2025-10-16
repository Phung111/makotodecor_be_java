package com.makotodecor.exceptions;

import org.springframework.http.HttpStatus;

import com.makotodecor.exceptions.base.DetailException;
import com.makotodecor.exceptions.base.ErrorMessage;

public class WebBadRequestException extends DetailException {

  public WebBadRequestException(ErrorMessage errorMessage) {
    super(errorMessage, HttpStatus.BAD_REQUEST);
  }
}