package com.makotodecor.exceptions;

import com.makotodecor.exceptions.base.DetailException;
import com.makotodecor.exceptions.base.ErrorMessage;
import org.springframework.http.HttpStatus;

public class FileUploadException extends DetailException {

  public FileUploadException() {
    super(ErrorMessage.FILE_UPLOAD_FAILED, HttpStatus.BAD_REQUEST);
  }

  public FileUploadException(ErrorMessage errorMessage) {
    super(errorMessage, HttpStatus.BAD_REQUEST);
  }

  public FileUploadException(ErrorMessage errorMessage, HttpStatus httpStatus) {
    super(errorMessage, httpStatus);
  }

  @Override
  public void addError(String target, ErrorMessage errorMessage) {
    super.addError(target, errorMessage);
  }
}
