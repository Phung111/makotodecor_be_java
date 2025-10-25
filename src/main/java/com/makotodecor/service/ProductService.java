package com.makotodecor.service;

import com.makotodecor.model.CreateProductRequest;
import com.makotodecor.model.ProductDetailResponse;
import com.makotodecor.model.ProductsPagedResponse;
import com.makotodecor.model.UpdateProductRequest;
import com.makotodecor.model.UpdateProductsStatusRequest;
import com.makotodecor.model.dto.ProductPagedCriteria;

public interface ProductService {

  ProductsPagedResponse getProductsPaged(ProductPagedCriteria criteria);

  ProductDetailResponse getProduct(Long productId);

  ProductDetailResponse createProduct(CreateProductRequest request);

  ProductDetailResponse updateProduct(Long productId, UpdateProductRequest request);

  void updateProductsStatus(UpdateProductsStatusRequest request);
}
