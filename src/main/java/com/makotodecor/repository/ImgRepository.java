package com.makotodecor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.makotodecor.model.entity.Img;

@Repository
public interface ImgRepository extends JpaRepository<Img, Long>, QuerydslPredicateExecutor<Img> {

  void deleteByProductId(Long productId);

  java.util.List<Img> findByProductId(Long productId);

}
