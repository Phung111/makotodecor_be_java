package com.makotodecor.controller;

import com.makotodecor.model.CreateProductRequest;
import com.makotodecor.model.ProductDetailResponse;
import com.makotodecor.model.ProductsPagedResponse;
import com.makotodecor.model.UpdateProductRequest;
import com.makotodecor.model.UpdateProductsStatusRequest;
import com.makotodecor.model.dto.ProductPagedCriteria;
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
      String keySearch, String status, Long categoryId, Long minPrice, Long maxPrice) {
    var criteria = ProductPagedCriteria.builder()
        .page(page)
        .size(size)
        .orderBy(orderBy)
        .keySearch(keySearch)
        .status(status)
        .categoryId(categoryId)
        .minPrice(minPrice)
        .maxPrice(maxPrice)
        .build();
    return ResponseEntity.ok(productService.getProductsPaged(criteria));
  }

  @Override
  public ResponseEntity<ProductDetailResponse> _getProduct(Long productId) {
    return ResponseEntity.ok(productService.getProduct(productId));
  }

  @Override
  @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
  public ResponseEntity<ProductDetailResponse> _createProduct(CreateProductRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
  }

  @Override
  @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
  public ResponseEntity<ProductDetailResponse> _updateProduct(Long productId, UpdateProductRequest request) {
    return ResponseEntity.ok(productService.updateProduct(productId, request));
  }

  @Override
  @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
  public ResponseEntity<String> _updateProductsStatus(UpdateProductsStatusRequest request) {
    productService.updateProductsStatus(request);
    return ResponseEntity.ok("Products status updated successfully");
  }
}
