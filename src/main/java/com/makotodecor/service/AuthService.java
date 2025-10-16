package com.makotodecor.service;

import com.makotodecor.model.ChangePasswordRequest;
import com.makotodecor.model.LoginRequest;
import com.makotodecor.model.RefreshTokenRequest;
import com.makotodecor.model.RegisterRequest;
import com.makotodecor.model.Token;

public interface AuthService {

  Token register(RegisterRequest request);

  Token login(LoginRequest request);

  Token refreshToken(RefreshTokenRequest request);

  void changePassword(ChangePasswordRequest request, String username);
}