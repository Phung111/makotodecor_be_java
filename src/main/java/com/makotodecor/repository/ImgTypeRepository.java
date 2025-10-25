package com.makotodecor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.makotodecor.model.entity.ImgType;

import java.util.Optional;

@Repository
public interface ImgTypeRepository extends JpaRepository<ImgType, Long>, QuerydslPredicateExecutor<ImgType> {

  Optional<ImgType> findByCode(String code);

  boolean existsByCode(String code);

}
