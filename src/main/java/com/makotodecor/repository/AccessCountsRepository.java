package com.makotodecor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.makotodecor.model.entity.AccessCounts;

@Repository
public interface AccessCountsRepository
    extends JpaRepository<AccessCounts, Long>, QuerydslPredicateExecutor<AccessCounts> {

}
