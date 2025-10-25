package com.makotodecor.controller;

import com.makotodecor.model.UpdateUserRequest;
import com.makotodecor.model.UpdateUsersStatusRequest;
import com.makotodecor.model.UserDetailResponse;
import com.makotodecor.model.UsersPagedResponse;
import com.makotodecor.model.dto.UserPagedCriteria;
import com.makotodecor.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UserController implements UserServiceApi {

  private final UserService userService;

  @Override
  public ResponseEntity<UsersPagedResponse> _getUsersPaged(Integer page, Integer size, String orderBy,
      String username, String email, String status, String role) {
    var criteria = UserPagedCriteria.builder()
        .page(page)
        .size(size)
        .orderBy(orderBy)
        .username(username)
        .email(email)
        .status(status)
        .role(role)
        .build();
    return ResponseEntity.ok(userService.getUsersPaged(criteria));
  }

  @Override
  public ResponseEntity<UserDetailResponse> _getUser(Long userId) {
    return ResponseEntity.ok(userService.getUser(userId));
  }

  @Override
  // @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
  public ResponseEntity<UserDetailResponse> _updateUser(Long userId, UpdateUserRequest request) {
    return ResponseEntity.ok(userService.updateUser(userId, request));
  }

  @Override
  // @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> _updateUsersStatus(UpdateUsersStatusRequest request) {
    userService.updateUsersStatus(request);
    return ResponseEntity.ok("Users status updated successfully");
  }

  @Override
  // @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> _deleteUser(Long userId) {
    userService.deleteUser(userId);
    return ResponseEntity.ok("User deleted successfully");
  }
}
