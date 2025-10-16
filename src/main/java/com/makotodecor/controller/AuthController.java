package com.makotodecor.controller;

import com.makotodecor.model.ChangePasswordRequest;
import com.makotodecor.model.LoginRequest;
import com.makotodecor.model.RefreshTokenRequest;
import com.makotodecor.model.RegisterRequest;
import com.makotodecor.model.Token;
import com.makotodecor.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import jakarta.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AuthController implements AuthServiceApi {

  private final AuthService authService;

  @Override
  public ResponseEntity<Token> _register(RegisterRequest request) {
    Token response = authService.register(request);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<Token> _login(LoginRequest request) {
    Token response = authService.login(request);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<Token> _refreshToken(RefreshTokenRequest request) {
    Token response = authService.refreshToken(request);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<String> _changePassword(@Valid ChangePasswordRequest changePasswordRequest) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    authService.changePassword(changePasswordRequest, username);
    return ResponseEntity.ok("Password changed successfully");
  }
}
