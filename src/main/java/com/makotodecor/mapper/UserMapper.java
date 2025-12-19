package com.makotodecor.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.makotodecor.model.UpdateUserRequest;
import com.makotodecor.model.UserDetailResponse;
import com.makotodecor.model.UserItemResponse;
import com.makotodecor.model.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserItemResponse toUserItemResponse(User user);

  UserDetailResponse toUserDetailResponse(User user);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "password", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "cart", ignore = true)
  @Mapping(target = "orders", ignore = true)
  User toUser(UpdateUserRequest request);

  User updateUserFromRequest(UpdateUserRequest request, @MappingTarget User user);




}
