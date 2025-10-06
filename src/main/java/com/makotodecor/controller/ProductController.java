package com.makotodecor.controller;

import com.makotodecor.model.entity.Product;
import com.makotodecor.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @GetMapping
  public ResponseEntity<Product> getProduct() {
    Product product = productService.getProduct();
    return ResponseEntity.ok(product);
  }
}
