package com.makotodecor.exceptions;

import org.springframework.http.HttpStatus;

import com.makotodecor.exceptions.base.DetailException;
import com.makotodecor.exceptions.base.ErrorMessage;

public class CategoryValidationException extends DetailException {

  public CategoryValidationException() {
    super(ErrorMessage.CATEGORY_INVALID, HttpStatus.BAD_REQUEST);
  }

  @Override
  public void addError(String target, ErrorMessage errorMessage) {
    super.addError(target, errorMessage);
  }
}

