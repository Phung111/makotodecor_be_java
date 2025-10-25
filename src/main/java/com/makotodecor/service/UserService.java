package com.makotodecor.service;

import com.makotodecor.model.UpdateUserRequest;
import com.makotodecor.model.UpdateUsersStatusRequest;
import com.makotodecor.model.UserDetailResponse;
import com.makotodecor.model.UsersPagedResponse;
import com.makotodecor.model.dto.UserPagedCriteria;

public interface UserService {

  UsersPagedResponse getUsersPaged(UserPagedCriteria criteria);

  UserDetailResponse getUser(Long userId);

  UserDetailResponse updateUser(Long userId, UpdateUserRequest request);

  void updateUsersStatus(UpdateUsersStatusRequest request);

  void deleteUser(Long userId);
}
