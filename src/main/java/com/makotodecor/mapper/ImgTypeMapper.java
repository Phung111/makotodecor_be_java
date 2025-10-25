package com.makotodecor.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.makotodecor.model.CreateImgTypeRequest;
import com.makotodecor.model.ImgTypeResponse;
import com.makotodecor.model.UpdateImgTypeRequest;
import com.makotodecor.model.entity.ImgType;

@Mapper(componentModel = "spring")
public interface ImgTypeMapper {

  // @Mapping(target = "status", expression =
  // "java(mapStatusToDto(imgType.getStatus()))")
  ImgTypeResponse toImgTypeResponse(ImgType imgType);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "imgs", ignore = true)
  // @Mapping(target = "status", expression =
  // "java(mapStatusFromDto(request.getStatus()))")
  ImgType toImgType(CreateImgTypeRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "imgs", ignore = true)
  // @Mapping(target = "status", expression =
  // "java(mapStatusFromDto(request.getStatus()))")
  ImgType toImgType(UpdateImgTypeRequest request);

  ImgType updateImgTypeFromRequest(UpdateImgTypeRequest request, @MappingTarget ImgType imgType);

  // default ImgTypeResponse.StatusEnum mapStatusToDto(
  // com.makotodecor.model.enums.ImgTypeStatusEnum status) {
  // if (status == null) {
  // return null;
  // }
  // return ImgTypeResponse.StatusEnum.fromValue(status.getValue());
  // }

  // default com.makotodecor.model.enums.ImgTypeStatusEnum mapStatusFromDto(
  // ImgTypeResponse.StatusEnum status) {
  // if (status == null) {
  // return null;
  // }
  // return
  // com.makotodecor.model.enums.ImgTypeStatusEnum.valueOf(status.getValue());
  // }
}
