package com.makotodecor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.makotodecor.model.entity.Color;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long>, QuerydslPredicateExecutor<Color> {

  void deleteByProductId(Long productId);

}
