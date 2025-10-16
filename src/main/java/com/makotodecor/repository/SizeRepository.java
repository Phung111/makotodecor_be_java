package com.makotodecor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.makotodecor.model.entity.Size;

@Repository
public interface SizeRepository extends JpaRepository<Size, Long>, QuerydslPredicateExecutor<Size> {

  void deleteByProductId(Long productId);

}
