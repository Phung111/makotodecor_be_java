package com.makotodecor.service;

import com.makotodecor.model.CategoriesPagedResponse;
import com.makotodecor.model.CategoryDetailResponse;
import com.makotodecor.model.CreateCategoryRequest;
import com.makotodecor.model.UpdateCategoryRequest;
import com.makotodecor.model.UpdateCategoriesStatusRequest;
import com.makotodecor.model.dto.CategoryPagedCriteria;

public interface CategoryService {

  CategoriesPagedResponse getCategoriesPaged(CategoryPagedCriteria criteria);

  CategoryDetailResponse getCategory(Long categoryId);

  CategoryDetailResponse createCategory(CreateCategoryRequest request);

  CategoryDetailResponse updateCategory(Long categoryId, UpdateCategoryRequest request);

  void updateCategoriesStatus(UpdateCategoriesStatusRequest request);

  void deleteCategory(Long categoryId);
}
