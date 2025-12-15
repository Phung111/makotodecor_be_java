package com.makotodecor.controller;

import com.makotodecor.model.AddCartItemRequest;
import com.makotodecor.model.CartItemIdRequest;
import com.makotodecor.model.CartCountResponse;
import com.makotodecor.model.CartResponse;
import com.makotodecor.model.ChangeQuantityCartItemRequest;
import com.makotodecor.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CartController implements CartServiceApi {

  private final CartService cartService;

  @Override
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<CartResponse> _getCart() {
    String username = getCurrentUsername();
    CartResponse response = cartService.getCart(username);
    return ResponseEntity.ok(response);
  }

  @Override
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<CartCountResponse> _getCartCount() {
    String username = getCurrentUsername();
    CartCountResponse response = cartService.getCartCount(username);
    return ResponseEntity.ok(response);
  }

  @Override
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<CartResponse> _addCartItem(AddCartItemRequest request) {
    String username = getCurrentUsername();
    CartResponse response = cartService.addCartItem(username, request);
    return ResponseEntity.ok(response);
  }

  @Override
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<CartResponse> _increaseCartItem(CartItemIdRequest request) {
    String username = getCurrentUsername();
    CartResponse response = cartService.increaseCartItem(username, request);
    return ResponseEntity.ok(response);
  }

  @Override
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<CartResponse> _decreaseCartItem(CartItemIdRequest request) {
    String username = getCurrentUsername();
    CartResponse response = cartService.decreaseCartItem(username, request);
    return ResponseEntity.ok(response);
  }

  @Override
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<CartResponse> _changeQuantityCartItem(ChangeQuantityCartItemRequest request) {
    String username = getCurrentUsername();
    CartResponse response = cartService.changeQuantityCartItem(username, request);
    return ResponseEntity.ok(response);
  }

  @Override
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<CartResponse> _deleteCartItem(CartItemIdRequest request) {
    String username = getCurrentUsername();
    CartResponse response = cartService.deleteCartItem(username, request);
    return ResponseEntity.ok(response);
  }

  private String getCurrentUsername() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new org.springframework.security.authentication.AuthenticationCredentialsNotFoundException("User not authenticated");
    }
    return authentication.getName();
  }
}
