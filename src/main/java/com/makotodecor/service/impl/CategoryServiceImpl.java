package com.makotodecor.service.impl;

import com.makotodecor.exceptions.WebBadRequestException;
import com.makotodecor.exceptions.base.ErrorMessage;
import com.makotodecor.mapper.CategoryMapper;
import com.makotodecor.model.CategoriesPagedResponse;
import com.makotodecor.model.CategoryDetailResponse;
import com.makotodecor.model.CreateCategoryRequest;
import com.makotodecor.model.UpdateCategoryRequest;
import com.makotodecor.model.UpdateCategoriesStatusRequest;
import com.makotodecor.model.dto.CategoryPagedCriteria;
import com.makotodecor.model.entity.Category;
import com.makotodecor.model.enums.CategoryStatusEnum;
import com.makotodecor.repository.CategoryRepository;
import com.makotodecor.service.CategoryService;
import com.makotodecor.util.PaginationUtils;
import com.makotodecor.util.QuerydslCriteriaUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  private static final Set<String> CATEGORY_SORTABLE_COLUMNS = Set.of("id", "code", "name", "status", "createdAt",
      "updatedAt");

  @Override
  public CategoriesPagedResponse getCategoriesPaged(CategoryPagedCriteria criteria) {
    var sortCriteria = PaginationUtils.parseSortCriteria(criteria.getOrderBy());
    PaginationUtils.validateSortColumns(sortCriteria, CATEGORY_SORTABLE_COLUMNS);

    var pageable = PageRequest
        .of(criteria.getPage(), criteria.getSize())
        .withSort(sortCriteria);

    var predicate = QuerydslCriteriaUtils.buildCategorySearchPredicate(criteria)
        .orElse(QuerydslCriteriaUtils.truePredicate());

    var pageResponse = categoryRepository.findAll(predicate, pageable);

    return CategoriesPagedResponse.builder()
        .pageInfo(PaginationUtils.toPageInfo(pageResponse))
        .items(pageResponse.getContent().stream()
            .map(categoryMapper::toCategoryItemResponse)
            .toList())
        .build();
  }

  @Override
  public CategoryDetailResponse getCategory(Long categoryId) {
    Category category = findCategoryById(categoryId);
    return categoryMapper.toCategoryDetailResponse(category);
  }

  @Override
  @Transactional
  public CategoryDetailResponse createCategory(CreateCategoryRequest request) {
    // Check if code already exists
    if (categoryRepository.existsByCode(request.getCode())) {
      throw new WebBadRequestException(ErrorMessage.CATEGORY_CODE_ALREADY_EXISTS);
    }

    Category category = categoryMapper.toCategory(request);
    category.setCreatedAt(ZonedDateTime.now());

    category = categoryRepository.save(category);

    return categoryMapper.toCategoryDetailResponse(category);
  }

  @Override
  @Transactional
  public CategoryDetailResponse updateCategory(Long categoryId, UpdateCategoryRequest request) {
    Category category = findCategoryById(categoryId);

    // Check if code already exists for different category
    if (request.getCode() != null && !request.getCode().equals(category.getCode())) {
      if (categoryRepository.existsByCode(request.getCode())) {
        throw new WebBadRequestException(ErrorMessage.CATEGORY_CODE_ALREADY_EXISTS);
      }
    }

    category = categoryMapper.updateCategoryFromRequest(request, category);

    category = categoryRepository.save(category);

    return categoryMapper.toCategoryDetailResponse(category);
  }

  @Override
  @Transactional
  public void updateCategoriesStatus(UpdateCategoriesStatusRequest request) {
    var categories = categoryRepository.findAllById(request.getCategoryIds());

    if (categories.size() != request.getCategoryIds().size()) {
      throw new WebBadRequestException(ErrorMessage.CATEGORY_NOT_FOUND);
    }

    var newStatus = CategoryStatusEnum.valueOf(request.getStatus().getValue());
    categories.forEach(category -> {
      category.setStatus(newStatus);
      category.setUpdatedAt(ZonedDateTime.now());
    });

    categoryRepository.saveAll(categories);
  }

  @Override
  @Transactional
  public void deleteCategory(Long categoryId) {
    Category category = findCategoryById(categoryId);

    // Check if category has products
    if (category.getProducts() != null && !category.getProducts().isEmpty()) {
      throw new WebBadRequestException(ErrorMessage.CATEGORY_HAS_PRODUCTS);
    }

    categoryRepository.delete(category);
  }

  private Category findCategoryById(Long categoryId) {
    return categoryRepository.findById(categoryId)
        .orElseThrow(() -> new WebBadRequestException(ErrorMessage.CATEGORY_NOT_FOUND));
  }
}
