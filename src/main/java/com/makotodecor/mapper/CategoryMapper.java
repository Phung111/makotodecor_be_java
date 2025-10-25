package com.makotodecor.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.makotodecor.model.CategoryDetailResponse;
import com.makotodecor.model.CategoryItemResponse;
import com.makotodecor.model.CreateCategoryRequest;
import com.makotodecor.model.UpdateCategoryRequest;
import com.makotodecor.model.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

  CategoryItemResponse toCategoryItemResponse(Category category);

  CategoryDetailResponse toCategoryDetailResponse(Category category);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "products", ignore = true)
  // @Mapping(target = "status", expression =
  // "java(mapStatusFromDto(request.getStatus()))")
  Category toCategory(CreateCategoryRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "products", ignore = true)
  // @Mapping(target = "status", expression =
  // "java(mapStatusFromDto(request.getStatus()))")
  Category toCategory(UpdateCategoryRequest request);

  Category updateCategoryFromRequest(UpdateCategoryRequest request, @MappingTarget Category category);

  // default com.makotodecor.model.CategoryStatusEnum mapStatusToDto(
  // com.makotodecor.model.enums.CategoryStatusEnum status) {
  // if (status == null) {
  // return null;
  // }
  // return com.makotodecor.model.CategoryStatusEnum.valueOf(status.name());
  // }

  // default com.makotodecor.model.enums.CategoryStatusEnum mapStatusFromDto(
  // com.makotodecor.model.CategoryStatusEnum status) {
  // if (status == null) {
  // return null;
  // }
  // return com.makotodecor.model.enums.CategoryStatusEnum.valueOf(status.name());
  // }

  // default com.makotodecor.model.enums.CategoryStatusEnum mapStatusFromDto(
  // com.makotodecor.model.CategoryStatusEnum status, Category category) {
  // if (status == null) {
  // return category.getStatus();
  // }
  // return com.makotodecor.model.enums.CategoryStatusEnum.valueOf(status.name());
  // }
}
