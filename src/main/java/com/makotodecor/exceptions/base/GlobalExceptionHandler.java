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

import java.util.List;

@lombok.Data
@lombok.AllArgsConstructor
class ErrorFieldDTO {
  private String code;
  private String message;
  private String target;
}

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final ResourceBundleMessageSource messageSource;

  @ExceptionHandler(DetailException.class)
  public ResponseEntity<ErrorResponse> handleDetailException(DetailException ex) {
    log.warn("A detail error occurred", ex);
    HttpStatus status = ex.getHttpStatus() == null ? HttpStatus.INTERNAL_SERVER_ERROR : ex.getHttpStatus();

    List<ErrorFieldDTO> translatedDetails = null;
    if (ex.hasErrors() && !ex.getDetails().isEmpty()) {
      translatedDetails = ex.getDetails().stream()
          .map(errorField -> new ErrorFieldDTO(
              errorField.getCode(),
              messageSource.getMessage(errorField.getMessage(), null, errorField.getMessage(),
                  LocaleContextHolder.getLocale()),
              errorField.getTarget()))
          .toList();
    }

    ErrorResponse.ErrorResponseBuilder responseBuilder = ErrorResponse.builder()
        .message(messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale()))
        .code(ex.getCode());

    ErrorResponse response = responseBuilder.build();

    if (translatedDetails != null && response.getClass().getDeclaredFields().length > 2) {
      try {
        java.lang.reflect.Field detailsField = response.getClass().getDeclaredField("details");
        detailsField.setAccessible(true);
        detailsField.set(response, translatedDetails);
      } catch (NoSuchFieldException | IllegalAccessException ignored) {
      }
    }

    return ResponseEntity.status(status).body(response);
  }
}
