package com.makotodecor.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.EntityGraph;

import jakarta.validation.constraints.NotNull;

import com.makotodecor.model.entity.Img;
import com.querydsl.core.types.Predicate;

@Repository
public interface ImgRepository extends JpaRepository<Img, Long>, QuerydslPredicateExecutor<Img> {

  void deleteByProductId(Long productId);

  java.util.List<Img> findByProductId(Long productId);

  java.util.Optional<Img> findFirstByUrl(String url);

  @NotNull
  @Override
  @EntityGraph(attributePaths = { "product", "imgType" })
  Page<Img> findAll(@NotNull Predicate predicate, @NotNull Pageable pageable);

}
