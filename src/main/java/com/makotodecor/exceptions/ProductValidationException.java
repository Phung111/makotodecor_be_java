package com.makotodecor.exceptions;

import org.springframework.http.HttpStatus;

import com.makotodecor.exceptions.base.DetailException;
import com.makotodecor.exceptions.base.ErrorMessage;

public class ProductValidationException extends DetailException {

  public ProductValidationException() {
    super(ErrorMessage.PRODUCT_INVALID, HttpStatus.BAD_REQUEST);
  }

  @Override
  public void addError(String target, ErrorMessage errorMessage) {
    super.addError(target, errorMessage);
  }
}
