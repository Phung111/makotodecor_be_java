package com.makotodecor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.makotodecor.model.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>, QuerydslPredicateExecutor<Cart> {

}
