package com.makotodecor.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.makotodecor.model.entity.Cart;
import com.makotodecor.model.entity.User;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>, QuerydslPredicateExecutor<Cart> {

  @EntityGraph(attributePaths = {
      "cartItems",
      "cartItems.product",
      "cartItems.product.imgs",
      "cartItems.size",
      "cartItems.color",
      "cartItems.color.img"
  })
  Optional<Cart> findByUser(User user);
}
