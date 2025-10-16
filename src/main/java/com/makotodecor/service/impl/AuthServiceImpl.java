package com.makotodecor.service.impl;

import com.makotodecor.model.ChangePasswordRequest;
import com.makotodecor.model.LoginRequest;
import com.makotodecor.model.RefreshTokenRequest;
import com.makotodecor.model.RegisterRequest;
import com.makotodecor.model.Token;
import com.makotodecor.model.entity.User;
import com.makotodecor.model.enums.RoleEnum;
import com.makotodecor.model.enums.UserStatusEnum;
import com.makotodecor.repository.UserRepository;
import com.makotodecor.service.AuthService;
import com.makotodecor.util.AuthValidationUtils;
import com.makotodecor.util.JwtUtil;
import com.makotodecor.exceptions.AuthValidationException;
import com.makotodecor.exceptions.base.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  private final AuthenticationManager authenticationManager;

  @Override
  public Token register(RegisterRequest request) {
    // Validate request
    AuthValidationUtils.validateRegisterRequest(request);

    // Check if user already exists
    if (userRepository.findByUsername(request.getUsername()).isPresent()) {
      throw new AuthValidationException(ErrorMessage.AUTH_USERNAME_ALREADY_EXISTS);
    }

    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
      throw new AuthValidationException(ErrorMessage.AUTH_EMAIL_ALREADY_EXISTS);
    }

    // Create new user
    User user = User.builder()
        .username(request.getUsername())
        .password(passwordEncoder.encode(request.getPassword()))
        .name(request.getName())
        .email(request.getEmail())
        .phone(request.getPhone())
        .role(RoleEnum.USER)
        .status(UserStatusEnum.ACTIVE)
        .createdAt(ZonedDateTime.now())
        .build();

    userRepository.save(user);

    // Generate JWT tokens
    String accessToken = jwtUtil.generateToken(user.getUsername(), user.getId(), user.getRole().name());
    String refreshToken = jwtUtil.generateRefreshToken(user.getUsername(), user.getId());
    ZonedDateTime expiresAt = ZonedDateTime.now().plusHours(jwtUtil.getExpirationHours()); // Configurable expiration
                                                                                           // hours

    return Token.builder()
        .token(accessToken)
        .refreshToken(refreshToken)
        .expiresAt(expiresAt)
        .build();
  }

  @Override
  public Token login(LoginRequest request) {
    // Validate request
    AuthValidationUtils.validateLoginRequest(request);

    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

      User user = userRepository.findByUsername(request.getUsername())
          .orElseThrow(() -> new AuthValidationException(ErrorMessage.AUTH_USER_NOT_FOUND));

      // Generate JWT tokens
      String accessToken = jwtUtil.generateToken(user.getUsername(), user.getId(), user.getRole().name());
      String refreshToken = jwtUtil.generateRefreshToken(user.getUsername(), user.getId());
      ZonedDateTime expiresAt = ZonedDateTime.now().plusHours(jwtUtil.getExpirationHours()); // Configurable expiration
                                                                                             // hours

      return Token.builder()
          .token(accessToken)
          .refreshToken(refreshToken)
          .expiresAt(expiresAt)
          .build();

    } catch (AuthenticationException e) {
      throw new AuthValidationException(ErrorMessage.AUTH_INVALID_CREDENTIALS);
    }
  }

  @Override
  public Token refreshToken(RefreshTokenRequest request) {
    // Validate request
    AuthValidationUtils.validateRefreshTokenRequest(request);

    try {
      // Validate refresh token
      String username = jwtUtil.extractUsernameFromRefreshToken(request.getRefreshToken());

      if (!jwtUtil.validateRefreshToken(request.getRefreshToken(), username)) {
        throw new AuthValidationException(ErrorMessage.AUTH_REFRESH_TOKEN_INVALID);
      }

      User user = userRepository.findByUsername(username)
          .orElseThrow(() -> new AuthValidationException(ErrorMessage.AUTH_USER_NOT_FOUND));

      // Generate new JWT tokens
      String accessToken = jwtUtil.generateToken(user.getUsername(), user.getId(), user.getRole().name());
      String refreshToken = jwtUtil.generateRefreshToken(user.getUsername(), user.getId());
      ZonedDateTime expiresAt = ZonedDateTime.now().plusHours(jwtUtil.getExpirationHours()); // Configurable expiration
                                                                                             // hours

      return Token.builder()
          .token(accessToken)
          .refreshToken(refreshToken)
          .expiresAt(expiresAt)
          .build();

    } catch (Exception e) {
      throw new AuthValidationException(ErrorMessage.AUTH_REFRESH_TOKEN_INVALID);
    }
  }

  @Override
  public void changePassword(ChangePasswordRequest request, String username) {
    // Validate request
    AuthValidationUtils.validateChangePasswordRequest(request);

    // Find user by username
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new AuthValidationException(ErrorMessage.AUTH_USER_NOT_FOUND));

    // Verify current password
    if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
      throw new AuthValidationException(ErrorMessage.AUTH_CURRENT_PASSWORD_INCORRECT);
    }

    // Validate new password confirmation (this is also checked in validation but
    // double-check for security)
    if (!request.getNewPassword().equals(request.getConfirmPassword())) {
      throw new AuthValidationException(ErrorMessage.AUTH_PASSWORDS_NOT_MATCH);
    }

    // Validate new password is different from current
    if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
      throw new AuthValidationException(ErrorMessage.AUTH_PASSWORD_SAME_AS_CURRENT);
    }

    // Update password
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    user.setUpdatedAt(ZonedDateTime.now());
    userRepository.save(user);
  }
}