package com.makotodecor.exceptions.base;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.makotodecor.model.ErrorResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final ResourceBundleMessageSource messageSource;

  @ExceptionHandler(DetailException.class)
  public ResponseEntity<ErrorResponse> handleDetailException(DetailException ex) {
    log.warn("A detail error occurred", ex);
    HttpStatus status = ex.getHttpStatus() == null ? HttpStatus.INTERNAL_SERVER_ERROR : ex.getHttpStatus();

    ErrorResponse response = ErrorResponse.builder()
        .message(messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale()))
        .code(ex.getCode())
        .build();

    return ResponseEntity.status(status).body(response);
  }
}
