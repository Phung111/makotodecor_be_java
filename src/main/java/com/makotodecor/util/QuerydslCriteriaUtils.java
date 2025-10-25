package com.makotodecor.util;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.makotodecor.model.dto.ProductPagedCriteria;
import com.makotodecor.model.dto.CategoryPagedCriteria;
import com.makotodecor.model.dto.UserPagedCriteria;
import com.makotodecor.model.entity.QProduct;
import com.makotodecor.model.entity.QCategory;
import com.makotodecor.model.entity.QUser;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.ExpressionUtils;

public class QuerydslCriteriaUtils {

  private QuerydslCriteriaUtils() {
    // Utility class
  }

  public static Optional<Predicate> buildProductSearchPredicate(ProductPagedCriteria criteria) {
    final QProduct qProduct = QProduct.product;

    final Stream<Supplier<Optional<Predicate>>> expressions = Stream.of(
        () -> eqIfNotNull(() -> containsIgnoreCaseDiacritics(qProduct.name, criteria.getName()), criteria.getName()));
    return buildPredicate(expressions);
  }

  public static Optional<Predicate> buildCategorySearchPredicate(CategoryPagedCriteria criteria) {
    final QCategory qCategory = QCategory.category;

    final Stream<Supplier<Optional<Predicate>>> expressions = Stream.of(
        () -> eqIfNotNull(() -> containsIgnoreCaseDiacritics(qCategory.name, criteria.getName()), criteria.getName()),
        () -> eqIfNotNull(
            () -> qCategory.status.eq(com.makotodecor.model.enums.CategoryStatusEnum.valueOf(criteria.getStatus())),
            criteria.getStatus()));
    return buildPredicate(expressions);
  }

  public static Optional<Predicate> buildUserSearchPredicate(UserPagedCriteria criteria) {
    final QUser qUser = QUser.user;

    final Stream<Supplier<Optional<Predicate>>> expressions = Stream.of(
        () -> eqIfNotNull(() -> containsIgnoreCaseDiacritics(qUser.username, criteria.getUsername()),
            criteria.getUsername()),
        () -> eqIfNotNull(() -> containsIgnoreCaseDiacritics(qUser.email, criteria.getEmail()), criteria.getEmail()),
        () -> eqIfNotNull(
            () -> qUser.status.eq(com.makotodecor.model.enums.UserStatusEnum.valueOf(criteria.getStatus())),
            criteria.getStatus()),
        () -> eqIfNotNull(() -> qUser.role.eq(com.makotodecor.model.enums.RoleEnum.valueOf(criteria.getRole())),
            criteria.getRole()));
    return buildPredicate(expressions);
  }

  private static Predicate containsIgnoreCaseDiacritics(com.querydsl.core.types.Expression<String> path,
      String value) {
    return Expressions.booleanTemplate(
        "unaccent(lower({0})) like concat('%', unaccent(lower({1})), '%')",
        path,
        value);
  }

  private static Optional<Predicate> eqIfNotNull(Supplier<Predicate> expression, Object value) {
    return value != null ? Optional.of(expression.get()) : Optional.empty();
  }

  public static Predicate truePredicate() {
    return Expressions.TRUE.isTrue();
  }

  private static Optional<Predicate> buildPredicate(Stream<Supplier<Optional<Predicate>>> expressions) {
    final Collection<Predicate> predicates = expressions
        .map(Supplier::get)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();

    return Optional.ofNullable(ExpressionUtils.allOf(predicates));
  }
}
