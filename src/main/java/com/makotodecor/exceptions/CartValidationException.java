package com.makotodecor.exceptions;

import org.springframework.http.HttpStatus;

import com.makotodecor.exceptions.base.DetailException;
import com.makotodecor.exceptions.base.ErrorMessage;

public class CartValidationException extends DetailException {

  public CartValidationException() {
    super(ErrorMessage.CART_INVALID, HttpStatus.BAD_REQUEST);
  }

  @Override
  public void addError(String target, ErrorMessage errorMessage) {
    super.addError(target, errorMessage);
  }
}
