package com.makotodecor.util;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.makotodecor.model.dto.ProductPagedCriteria;
import com.makotodecor.model.entity.QProduct;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.ExpressionUtils;

public class QuerydslCriteriaUtils {

  public static Optional<Predicate> buildProductSearchPredicate(ProductPagedCriteria criteria) {
    final QProduct qProduct = QProduct.product;

    final Stream<Supplier<Optional<Predicate>>> expressions = Stream.of(
        () -> eqIfNotNull(() -> containsIgnoreCaseDiacritics(qProduct.name, criteria.getName()), criteria.getName()));
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
