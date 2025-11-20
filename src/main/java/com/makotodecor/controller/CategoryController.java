package com.makotodecor.controller;

import com.makotodecor.model.CategoriesPagedResponse;
import com.makotodecor.model.CategoryDetailResponse;
import com.makotodecor.model.CreateCategoryRequest;
import com.makotodecor.model.UpdateCategoryRequest;
import com.makotodecor.model.UpdateCategoriesStatusRequest;
import com.makotodecor.model.dto.CategoryPagedCriteria;
import com.makotodecor.service.CategoryService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CategoryController implements CategoryServiceApi {

  private final CategoryService categoryService;

  @Override
  public ResponseEntity<CategoriesPagedResponse> _getCategoriesPaged(Integer page, Integer size, String orderBy,
      String name, String status) {
    var criteria = CategoryPagedCriteria.builder()
        .page(page)
        .size(size)
        .orderBy(orderBy)
        .name(name)
        .status(status)
        .build();
    return ResponseEntity.ok(categoryService.getCategoriesPaged(criteria));
  }

  @Override
  public ResponseEntity<CategoryDetailResponse> _getCategory(Long categoryId) {
    return ResponseEntity.ok(categoryService.getCategory(categoryId));
  }

  @Override
  @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
  public ResponseEntity<CategoryDetailResponse> _createCategory(CreateCategoryRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(request));
  }

  @Override
  @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
  public ResponseEntity<CategoryDetailResponse> _updateCategory(Long categoryId, UpdateCategoryRequest request) {
    return ResponseEntity.ok(categoryService.updateCategory(categoryId, request));
  }

  @Override
  @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
  public ResponseEntity<String> _updateCategoriesStatus(UpdateCategoriesStatusRequest request) {
    categoryService.updateCategoriesStatus(request);
    return ResponseEntity.ok("Categories status updated successfully");
  }

  @Override
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> _deleteCategory(Long categoryId) {
    categoryService.deleteCategory(categoryId);
    return ResponseEntity.ok("Category deleted successfully");
  }
}
