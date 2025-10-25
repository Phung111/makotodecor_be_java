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

  // @Mapping(target = "status", expression =
  // "java(mapStatusToDto(user.getStatus()))")
  // @Mapping(target = "role", expression = "java(mapRoleToDto(user.getRole()))")
  UserItemResponse toUserItemResponse(User user);

  // @Mapping(target = "status", expression =
  // "java(mapStatusToDto(user.getStatus()))")
  // @Mapping(target = "role", expression = "java(mapRoleToDto(user.getRole()))")
  UserDetailResponse toUserDetailResponse(User user);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "password", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "cart", ignore = true)
  @Mapping(target = "orders", ignore = true)
  // @Mapping(target = "status", expression =
  // "java(mapStatusFromDto(request.getStatus()))")
  // @Mapping(target = "role", expression =
  // "java(mapRoleFromDto(request.getRole()))")
  User toUser(UpdateUserRequest request);

  User updateUserFromRequest(UpdateUserRequest request, @MappingTarget User user);

  // default com.makotodecor.model.UserStatusEnum mapStatusToDto(
  // com.makotodecor.model.enums.UserStatusEnum status) {
  // if (status == null) {
  // return null;
  // }
  // return com.makotodecor.model.UserStatusEnum.valueOf(status.name());
  // }

  // default com.makotodecor.model.RoleEnum mapRoleToDto(
  // com.makotodecor.model.enums.RoleEnum role) {
  // if (role == null) {
  // return null;
  // }
  // return com.makotodecor.model.RoleEnum.valueOf(role.name());
  // }

  // default com.makotodecor.model.enums.UserStatusEnum mapStatusFromDto(
  // com.makotodecor.model.UserStatusEnum status) {
  // if (status == null) {
  // return null;
  // }
  // return com.makotodecor.model.enums.UserStatusEnum.valueOf(status.name());
  // }

  // default com.makotodecor.model.enums.RoleEnum mapRoleFromDto(
  // com.makotodecor.model.RoleEnum role) {
  // if (role == null) {
  // return null;
  // }
  // return com.makotodecor.model.enums.RoleEnum.valueOf(role.name());
  // }
}
