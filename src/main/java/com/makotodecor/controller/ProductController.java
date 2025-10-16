package com.makotodecor.controller;

import com.makotodecor.model.CreateProductRequest;
import com.makotodecor.model.ProductDetailResponse;
import com.makotodecor.model.ProductsPagedResponse;
import com.makotodecor.model.UpdateProductRequest;
import com.makotodecor.model.dto.ProductPagedCriteria;
import com.makotodecor.model.enums.RoleEnum;
import com.makotodecor.service.ProductService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ProductController implements ProductServiceApi {

  private final ProductService productService;

  @Override
  public ResponseEntity<ProductsPagedResponse> _getProductsPaged(Integer page, Integer size, String orderBy,
      String name, String status, String category) {
    var criteria = ProductPagedCriteria.builder()
        .page(page)
        .size(size)
        .orderBy(orderBy)
        .name(name)
        .status(status)
        .category(category)
        .build();
    return ResponseEntity.ok(productService.getProductsPaged(criteria));
  }

  @Override
  public ResponseEntity<ProductDetailResponse> _getProduct(Long productId) {
    return ResponseEntity.ok(productService.getProduct(productId));
  }

  @Override
  // @PreAuthorize(RoleEnum.Auth.ADMIN)
  public ResponseEntity<ProductDetailResponse> _createProduct(CreateProductRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
  }

  @Override
  // @PreAuthorize(RoleEnum.Auth.ADMIN)
  public ResponseEntity<ProductDetailResponse> _updateProduct(Long productId, UpdateProductRequest request) {
    return ResponseEntity.ok(productService.updateProduct(productId, request));
  }
}
