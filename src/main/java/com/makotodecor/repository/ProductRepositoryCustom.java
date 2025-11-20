package com.makotodecor.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.querydsl.core.types.Predicate;
import com.makotodecor.model.entity.Product;

public interface ProductRepositoryCustom {
  Page<Product> findAllWithSpecialSort(Predicate predicate, Pageable pageable);
}

