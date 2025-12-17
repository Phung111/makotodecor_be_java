package com.makotodecor.controller;

import com.makotodecor.model.CreateOrderRequest;
import com.makotodecor.model.OrderDetailResponse;
import com.makotodecor.model.OrdersPagedResponse;
import com.makotodecor.model.UpdateOrderStatusRequest;
import com.makotodecor.model.dto.OrderPagedCriteria;
import com.makotodecor.service.OrderService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class OrderController implements OrderServiceApi {

  private final OrderService orderService;

  @Override
  @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
  public ResponseEntity<OrdersPagedResponse> _getOrdersPaged(Integer page, Integer size, String orderBy,
      String status, Long userId) {
    var criteria = OrderPagedCriteria.builder()
        .page(page)
        .size(size)
        .orderBy(orderBy)
        .status(status)
        .userId(userId)
        .build();
    return ResponseEntity.ok(orderService.getOrdersPaged(criteria));
  }

  @Override
  @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
  public ResponseEntity<OrderDetailResponse> _getOrder(Long orderId) {
    return ResponseEntity.ok(orderService.getOrder(orderId));
  }

  @Override
  @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
  public ResponseEntity<String> _updateOrderStatus(UpdateOrderStatusRequest request) {
    orderService.updateOrderStatus(request);
    return ResponseEntity.ok("Order status updated successfully");
  }

  @Override
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<OrderDetailResponse> _placeOrder(CreateOrderRequest request) {
    String username = getCurrentUsername();
    return ResponseEntity.ok(orderService.placeOrder(request, username));
  }

  @Override
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<OrdersPagedResponse> _getMyOrdersPaged(Integer page, Integer size, String orderBy,
      String status) {
    String username = getCurrentUsername();
    var criteria = OrderPagedCriteria.builder()
        .page(page)
        .size(size)
        .orderBy(orderBy)
        .status(status)
        .build();
    return ResponseEntity.ok(orderService.getMyOrdersPaged(criteria, username));
  }

  @Override
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<OrderDetailResponse> _getMyOrder(Long orderId) {
    String username = getCurrentUsername();
    return ResponseEntity.ok(orderService.getMyOrder(orderId, username));
  }

  private String getCurrentUsername() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new org.springframework.security.authentication.AuthenticationCredentialsNotFoundException("User not authenticated");
    }
    return authentication.getName();
  }
}
