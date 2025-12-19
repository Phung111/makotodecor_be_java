package com.makotodecor.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.makotodecor.model.CreateImgRequest;
import com.makotodecor.model.ImgDetailResponse;
import com.makotodecor.model.ImgItemResponse;
import com.makotodecor.model.UpdateImgRequest;
import com.makotodecor.model.entity.Img;

@Mapper(componentModel = "spring")
public interface ImgMapper {

  @Mapping(target = "imgTypeId", source = "imgType.id")
  ImgItemResponse toImgItemResponse(Img img);

  @Mapping(target = "product", source = "img.product")
  @Mapping(target = "imgType", source = "img.imgType")
  ImgDetailResponse toImgDetailResponse(Img img);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "product", ignore = true)
  @Mapping(target = "imgType", ignore = true)
  Img toImg(CreateImgRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "product", ignore = true)
  @Mapping(target = "imgType", ignore = true)
  Img updateImgFromRequest(UpdateImgRequest request, @org.mapstruct.MappingTarget Img img);


}
