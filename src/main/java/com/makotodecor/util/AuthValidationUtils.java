package com.makotodecor.util;

import com.makotodecor.exceptions.AuthValidationException;
import com.makotodecor.exceptions.base.ErrorMessage;
import com.makotodecor.model.ChangePasswordRequest;
import com.makotodecor.model.LoginRequest;
import com.makotodecor.model.RefreshTokenRequest;
import com.makotodecor.model.RegisterRequest;

import java.util.regex.Pattern;

public class AuthValidationUtils {

  private static final Pattern EMAIL_PATTERN = Pattern.compile(
      "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

  private static final Pattern PHONE_PATTERN = Pattern.compile(
      "^(\\+84|0)[0-9]{9,10}$");

  private static final Pattern PASSWORD_PATTERN = Pattern.compile(
      "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{8,}$");

  public static void validateRegisterRequest(RegisterRequest request) {
    AuthValidationException exception = AuthValidationException.withMultipleErrors();

    if (isNullOrEmpty(request.getUsername())) {
      exception.addError("username", ErrorMessage.AUTH_USERNAME_REQUIRED);
    } else if (request.getUsername().length() < 3 || request.getUsername().length() > 50) {
      exception.addError("username", ErrorMessage.AUTH_USERNAME_INVALID);
    }

    if (isNullOrEmpty(request.getPassword())) {
      exception.addError("password", ErrorMessage.AUTH_PASSWORD_REQUIRED);
    } else if (request.getPassword().length() < 8) {
      exception.addError("password", ErrorMessage.AUTH_PASSWORD_TOO_SHORT);
    } else if (!PASSWORD_PATTERN.matcher(request.getPassword()).matches()) {
      exception.addError("password", ErrorMessage.AUTH_PASSWORD_TOO_WEAK);
    }

    if (isNullOrEmpty(request.getEmail())) {
      exception.addError("email", ErrorMessage.AUTH_EMAIL_REQUIRED);
    } else if (!EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
      exception.addError("email", ErrorMessage.AUTH_EMAIL_INVALID);
    }

    if (isNullOrEmpty(request.getName())) {
      exception.addError("name", ErrorMessage.AUTH_NAME_REQUIRED);
    }

    if (!isNullOrEmpty(request.getPhone()) && !PHONE_PATTERN.matcher(request.getPhone()).matches()) {
      exception.addError("phone", ErrorMessage.AUTH_PHONE_INVALID);
    }

    if (exception.hasErrors()) {
      throw exception;
    }
  }

  public static void validateLoginRequest(LoginRequest request) {
    AuthValidationException exception = AuthValidationException.withMultipleErrors();

    if (isNullOrEmpty(request.getUsername())) {
      exception.addError("username", ErrorMessage.AUTH_USERNAME_REQUIRED);
    }

    if (isNullOrEmpty(request.getPassword())) {
      exception.addError("password", ErrorMessage.AUTH_PASSWORD_REQUIRED);
    }

    if (exception.hasErrors()) {
      throw exception;
    }
  }

  public static void validateRefreshTokenRequest(RefreshTokenRequest request) {
    AuthValidationException exception = AuthValidationException.withMultipleErrors();

    if (isNullOrEmpty(request.getRefreshToken())) {
      exception.addError("refreshToken", ErrorMessage.AUTH_REFRESH_TOKEN_REQUIRED);
    } else if (request.getRefreshToken().length() < 10) {
      exception.addError("refreshToken", ErrorMessage.AUTH_REFRESH_TOKEN_INVALID);
    }

    if (exception.hasErrors()) {
      throw exception;
    }
  }

  public static void validateChangePasswordRequest(ChangePasswordRequest request) {
    AuthValidationException exception = AuthValidationException.withMultipleErrors();

    if (isNullOrEmpty(request.getCurrentPassword())) {
      exception.addError("currentPassword", ErrorMessage.AUTH_CURRENT_PASSWORD_REQUIRED);
    }

    if (isNullOrEmpty(request.getNewPassword())) {
      exception.addError("newPassword", ErrorMessage.AUTH_NEW_PASSWORD_REQUIRED);
    } else if (request.getNewPassword().length() < 8) {
      exception.addError("newPassword", ErrorMessage.AUTH_PASSWORD_TOO_SHORT);
    } else if (!PASSWORD_PATTERN.matcher(request.getNewPassword()).matches()) {
      exception.addError("newPassword", ErrorMessage.AUTH_PASSWORD_TOO_WEAK);
    }

    if (isNullOrEmpty(request.getConfirmPassword())) {
      exception.addError("confirmPassword", ErrorMessage.AUTH_CONFIRM_PASSWORD_REQUIRED);
    } else if (!request.getNewPassword().equals(request.getConfirmPassword())) {
      exception.addError("confirmPassword", ErrorMessage.AUTH_PASSWORDS_NOT_MATCH);
    }

    if (!isNullOrEmpty(request.getCurrentPassword()) &&
        !isNullOrEmpty(request.getNewPassword()) &&
        request.getCurrentPassword().equals(request.getNewPassword())) {
      exception.addError("newPassword", ErrorMessage.AUTH_PASSWORD_SAME_AS_CURRENT);
    }

    if (exception.hasErrors()) {
      throw exception;
    }
  }

  private static boolean isNullOrEmpty(String str) {
    return str == null || str.trim().isEmpty();
  }
}
