package com.makotodecor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.makotodecor.model.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, QuerydslPredicateExecutor<Order> {

}
