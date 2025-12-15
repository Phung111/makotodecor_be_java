package com.makotodecor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.makotodecor.model.entity.Cart;
import com.makotodecor.model.entity.CartItem;
import com.makotodecor.model.entity.Color;
import com.makotodecor.model.entity.Product;
import com.makotodecor.model.entity.Size;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>, QuerydslPredicateExecutor<CartItem> {

  Optional<CartItem> findByCartAndProductAndSizeAndColor(Cart cart, Product product, Size size, Color color);
}
