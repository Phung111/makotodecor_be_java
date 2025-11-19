package com.makotodecor.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.makotodecor.model.CategoryDetailResponse;
import com.makotodecor.model.CategoryItemResponse;
import com.makotodecor.model.CategoryImage;
import com.makotodecor.model.CreateCategoryRequest;
import com.makotodecor.model.UpdateCategoryRequest;
import com.makotodecor.model.entity.Category;
import com.makotodecor.model.entity.Img;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

  @Mapping(target = "img", expression = "java(mapCategoryImage(category))")
  @Mapping(target = "status", expression = "java(mapStatusToDto(category.getStatus()))")
  CategoryItemResponse toCategoryItemResponse(Category category);

  @Mapping(target = "img", expression = "java(mapCategoryImage(category))")
  CategoryDetailResponse toCategoryDetailResponse(Category category);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "products", ignore = true)
  @Mapping(target = "img", ignore = true)
  Category toCategory(CreateCategoryRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "products", ignore = true)
  @Mapping(target = "img", ignore = true)
  Category updateCategoryFromRequest(UpdateCategoryRequest request, @MappingTarget Category category);

  default CategoryImage mapCategoryImage(Category category) {
    if (category == null) {
      return null;
    }
    Img img = category.getImg();
    if (img == null) {
      return null;
    }
    return CategoryImage.builder()
        .id(img.getId())
        .url(img.getUrl())
        .build();
  }

  default com.makotodecor.model.CategoryStatusEnum mapStatusToDto(
      com.makotodecor.model.enums.CategoryStatusEnum status) {
    if (status == null) {
      return null;
    }
    return com.makotodecor.model.CategoryStatusEnum.valueOf(status.name());
  }
}
