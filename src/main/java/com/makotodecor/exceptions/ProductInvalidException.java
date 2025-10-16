package com.makotodecor.exceptions;

import org.springframework.http.HttpStatus;

import com.makotodecor.exceptions.base.DetailException;
import com.makotodecor.exceptions.base.ErrorMessage;

public class ProductInvalidException extends DetailException {

  public ProductInvalidException() {
    super(ErrorMessage.PRODUCT_INVALID, HttpStatus.BAD_REQUEST);
  }
}
