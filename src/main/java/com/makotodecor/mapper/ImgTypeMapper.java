package com.makotodecor.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.makotodecor.model.CreateImgTypeRequest;
import com.makotodecor.model.ImgTypeItemResponse;
import com.makotodecor.model.ImgTypeResponse;
import com.makotodecor.model.UpdateImgTypeRequest;
import com.makotodecor.model.entity.ImgType;

@Mapper(componentModel = "spring")
public interface ImgTypeMapper {

  ImgTypeResponse toImgTypeResponse(ImgType imgType);

  @Mapping(target = "status", expression = "java(mapStatusToDto(imgType.getStatus()))")
  ImgTypeItemResponse toImgTypeItemResponse(ImgType imgType);

  default com.makotodecor.model.ImgTypeStatusEnum mapStatusToDto(
      com.makotodecor.model.enums.ImgTypeStatusEnum status) {
    if (status == null) {
      return null;
    }
    return com.makotodecor.model.ImgTypeStatusEnum.valueOf(status.name());
  }

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "imgs", ignore = true)
  ImgType toImgType(CreateImgTypeRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "imgs", ignore = true)
  ImgType toImgType(UpdateImgTypeRequest request);

  ImgType updateImgTypeFromRequest(UpdateImgTypeRequest request, @MappingTarget ImgType imgType);


}
