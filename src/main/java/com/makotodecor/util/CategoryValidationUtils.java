package com.makotodecor.util;

import com.makotodecor.exceptions.CategoryValidationException;
import com.makotodecor.exceptions.base.ErrorMessage;
import com.makotodecor.model.CategoryImage;
import com.makotodecor.model.CreateCategoryRequest;
import com.makotodecor.model.UpdateCategoryRequest;

import java.util.Arrays;

public class CategoryValidationUtils {

  private static final int MIN_CODE_LENGTH = 1;
  private static final int MAX_CODE_LENGTH = 50;
  private static final int MIN_NAME_LENGTH = 1;
  private static final int MAX_NAME_LENGTH = 255;

  private CategoryValidationUtils() {
  }

  public static void validateCreateCategoryRequest(CreateCategoryRequest request) {
    CategoryValidationException exception = new CategoryValidationException();

    validateCode(request.getCode(), "code", exception);
    validateName(request.getName(), "name", exception);
    validateStatus(request.getStatus(), "status", exception);

    if (request.getImg() != null) {
      validateCategoryImage(request.getImg(), "img", exception);
    }

    if (exception.hasErrors()) {
      throw exception;
    }
  }

  public static void validateUpdateCategoryRequest(UpdateCategoryRequest request) {
    CategoryValidationException exception = new CategoryValidationException();

    if (request.getCode() != null) {
      validateCode(request.getCode(), "code", exception);
    }

    if (request.getName() != null) {
      validateName(request.getName(), "name", exception);
    }

    if (request.getStatus() != null) {
      validateStatus(request.getStatus(), "status", exception);
    }

    if (request.getImg() != null) {
      validateCategoryImage(request.getImg(), "img", exception);
    }

    if (exception.hasErrors()) {
      throw exception;
    }
  }

  private static void validateCode(String code, String target, CategoryValidationException exception) {
    if (code == null || code.trim().isEmpty()) {
      exception.addError(target, ErrorMessage.CATEGORY_CODE_REQUIRED);
      return;
    }

    int length = code.trim().length();
    if (length < MIN_CODE_LENGTH || length > MAX_CODE_LENGTH) {
      exception.addError(target, ErrorMessage.CATEGORY_CODE_INVALID);
    }
  }

  private static void validateName(String name, String target, CategoryValidationException exception) {
    if (name == null || name.trim().isEmpty()) {
      exception.addError(target, ErrorMessage.CATEGORY_NAME_REQUIRED);
      return;
    }

    int length = name.trim().length();
    if (length < MIN_NAME_LENGTH || length > MAX_NAME_LENGTH) {
      exception.addError(target, ErrorMessage.CATEGORY_NAME_INVALID);
    }
  }

  private static void validateStatus(Object status, String target, CategoryValidationException exception) {
    if (status == null) {
      exception.addError(target, ErrorMessage.CATEGORY_STATUS_REQUIRED);
      return;
    }

    String statusValue = status.toString();
    boolean isValid = Arrays.stream(com.makotodecor.model.enums.CategoryStatusEnum.values())
        .anyMatch(e -> e.getValue().equals(statusValue));

    if (!isValid) {
      exception.addError(target, ErrorMessage.CATEGORY_STATUS_INVALID);
    }
  }

  private static void validateCategoryImage(CategoryImage image, String target, CategoryValidationException exception) {
    if (image.getUrl() == null || image.getUrl().trim().isEmpty()) {
      exception.addError(target + ".url", ErrorMessage.CATEGORY_IMAGE_URL_REQUIRED);
    }
  }
}

