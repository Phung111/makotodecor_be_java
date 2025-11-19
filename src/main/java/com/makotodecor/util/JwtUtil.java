package com.makotodecor.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

  private static final String USER_ID_CLAIM = "userId";
  private static final String ROLE_CLAIM = "role";
  private static final String TYPE_CLAIM = "type";
  private static final String REFRESH_TYPE = "refresh";

  @Value("${jwt.secret:mySecretKey}")
  private String secret;

  @Value("${jwt.expiration:86400000}")
  private Long expiration;

  @Value("${jwt.refresh-expiration:604800000}")
  private Long refreshExpiration;

  @Value("${jwt.expiration-hours:6}")
  private Integer expirationHours;

  private SecretKey getSigningKey() {
    try {
      // Hash the secret to ensure it's at least 256 bits (32 bytes) for HMAC-SHA256
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] keyBytes = digest.digest(secret.getBytes());
      return Keys.hmacShaKeyFor(keyBytes);
    } catch (NoSuchAlgorithmException e) {
      // Fallback: if SHA-256 is not available, pad the secret to at least 32 bytes
      byte[] secretBytes = secret.getBytes();
      if (secretBytes.length < 32) {
        byte[] paddedKey = new byte[32];
        System.arraycopy(secretBytes, 0, paddedKey, 0, secretBytes.length);
        // Repeat the secret to fill the remaining bytes
        for (int i = secretBytes.length; i < 32; i++) {
          paddedKey[i] = secretBytes[i % secretBytes.length];
        }
        return Keys.hmacShaKeyFor(paddedKey);
      }
      return Keys.hmacShaKeyFor(secretBytes);
    }
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public String generateToken(String username, Long userId, String role) {
    Map<String, Object> claims = new HashMap<>();
    claims.put(USER_ID_CLAIM, userId);
    claims.put(ROLE_CLAIM, role);
    return createToken(claims, username, expiration);
  }

  public String generateRefreshToken(String username, Long userId) {
    Map<String, Object> claims = new HashMap<>();
    claims.put(USER_ID_CLAIM, userId);
    claims.put(TYPE_CLAIM, REFRESH_TYPE);
    return createToken(claims, username, refreshExpiration);
  }

  private String createToken(Map<String, Object> claims, String subject, Long expirationTime) {
    return Jwts.builder()
        .claims(claims)
        .subject(subject)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + expirationTime))
        .signWith(getSigningKey())
        .compact();
  }

  public Boolean validateToken(String token, String username) {
    final String extractedUsername = extractUsername(token);
    return (extractedUsername.equals(username) && !isTokenExpired(token));
  }

  public Long extractUserId(String token) {
    return extractClaim(token, claims -> claims.get(USER_ID_CLAIM, Long.class));
  }

  public String extractRole(String token) {
    return extractClaim(token, claims -> claims.get(ROLE_CLAIM, String.class));
  }

  public Boolean validateRefreshToken(String token, String username) {
    final String extractedUsername = extractUsername(token);
    String tokenType = extractClaim(token, claims -> claims.get(TYPE_CLAIM, String.class));
    return (extractedUsername.equals(username) && !isTokenExpired(token) && REFRESH_TYPE.equals(tokenType));
  }

  public Long extractUserIdFromRefreshToken(String token) {
    return extractClaim(token, claims -> claims.get(USER_ID_CLAIM, Long.class));
  }

  public String extractUsernameFromRefreshToken(String token) {
    return extractUsername(token);
  }

  public Integer getExpirationHours() {
    return expirationHours;
  }
}
