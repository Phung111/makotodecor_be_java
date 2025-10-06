package com.makotodecor.service.impl;

import com.makotodecor.model.entity.Product;
import com.makotodecor.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    @Override
    public Product getProduct() {
        return Product.builder()
                .id(1L)
                .name("Makotodecor")
                .build();
    }
}
