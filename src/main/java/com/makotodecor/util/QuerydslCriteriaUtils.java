package com.makotodecor.util;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.makotodecor.model.dto.ProductPagedCriteria;
import com.makotodecor.model.dto.CategoryPagedCriteria;
import com.makotodecor.model.dto.UserPagedCriteria;
import com.makotodecor.model.dto.ImgTypePagedCriteria;
import com.makotodecor.model.dto.OrderPagedCriteria;
import com.makotodecor.model.dto.ImgPagedCriteria;
import com.makotodecor.model.entity.QProduct;
import com.makotodecor.model.entity.QCategory;
import com.makotodecor.model.entity.QUser;
import com.makotodecor.model.entity.QImgType;
import com.makotodecor.model.entity.QOrder;
import com.makotodecor.model.entity.QImg;
import com.makotodecor.model.enums.ProductStatusEnum;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.ExpressionUtils;

public class QuerydslCriteriaUtils {

  private QuerydslCriteriaUtils() {
  }

  public static Optional<Predicate> buildProductSearchPredicate(ProductPagedCriteria criteria) {
    final QProduct qProduct = QProduct.product;

    final Stream<Supplier<Optional<Predicate>>> expressions = Stream.of(
        () -> eqIfNotNull(() -> containsIgnoreCaseDiacritics(qProduct.name, criteria.getKeySearch()),
            criteria.getKeySearch()),
        () -> eqIfNotNull(() -> qProduct.category().id.eq(criteria.getCategoryId()), criteria.getCategoryId()),
        () -> eqIfNotNull(() -> qProduct.sizes.any().price.goe(criteria.getMinPrice()), criteria.getMinPrice()),
        () -> eqIfNotNull(() -> qProduct.sizes.any().price.loe(criteria.getMaxPrice()), criteria.getMaxPrice()),
        () -> eqIfNotNull(() -> qProduct.status.eq(ProductStatusEnum.valueOf(criteria.getStatus())),
            criteria.getStatus()));
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

  public static Optional<Predicate> buildImgTypeSearchPredicate(ImgTypePagedCriteria criteria) {
    final QImgType qImgType = QImgType.imgType;

    final Stream<Supplier<Optional<Predicate>>> expressions = Stream.of(
        () -> eqIfNotNull(() -> containsIgnoreCaseDiacritics(qImgType.name, criteria.getName()), criteria.getName()),
        () -> eqIfNotNull(() -> containsIgnoreCaseDiacritics(qImgType.code, criteria.getCode()), criteria.getCode()),
        () -> eqIfNotNull(
            () -> qImgType.status.eq(com.makotodecor.model.enums.ImgTypeStatusEnum.valueOf(criteria.getStatus())),
            criteria.getStatus()));
    return buildPredicate(expressions);
  }

  public static Optional<Predicate> buildOrderSearchPredicate(OrderPagedCriteria criteria) {
    final QOrder qOrder = QOrder.order;

    Optional<Predicate> keySearchOrPredicate = buildOrPredicate(Stream.of(
        () -> eqIfNotNull(() -> containsIgnoreCaseDiacritics(qOrder.code, criteria.getKeySearch()), criteria.getKeySearch()),
        () -> eqIfNotNull(() -> containsIgnoreCaseDiacritics(qOrder.user().username, criteria.getKeySearch()), criteria.getKeySearch()),
        () -> eqIfNotNull(() -> containsIgnoreCaseDiacritics(qOrder.user().email, criteria.getKeySearch()), criteria.getKeySearch())
    ));

    final Stream<Supplier<Optional<Predicate>>> expressions = Stream.of(
        () -> keySearchOrPredicate,
        () -> eqIfNotNull(
            () -> qOrder.status.eq(com.makotodecor.model.enums.OrderStatusEnum.valueOf(criteria.getStatus())),
            criteria.getStatus()),
        () -> eqIfNotNull(() -> qOrder.user().id.eq(criteria.getUserId()), criteria.getUserId()));
    return buildPredicate(expressions);
    
  }
  public static Optional<Predicate> buildImgSearchPredicate(ImgPagedCriteria criteria) {
    final QImg qImg = QImg.img;

    final Stream<Supplier<Optional<Predicate>>> expressions = Stream.of(
        () -> eqIfNotNull(() -> qImg.imgType().id.eq(criteria.getImgTypeId()), criteria.getImgTypeId()),
        () -> eqIfNotNull(() -> qImg.product().id.eq(criteria.getProductId()), criteria.getProductId()),
        () -> eqIfNotNull(() -> qImg.isDefault.eq(criteria.getIsDefault()), criteria.getIsDefault()));
    return buildPredicate(expressions);
  }

  private static Predicate containsIgnoreCaseDiacritics(com.querydsl.core.types.Expression<String> path,
      String value) {
    StringExpression pathExpr = Expressions.stringTemplate("unaccent(lower({0}))", path);
    StringExpression valueExpr = Expressions.stringTemplate("unaccent(lower({0}))", Expressions.constant(value));
    StringExpression patternExpr = Expressions.stringTemplate("concat('%', {0}, '%')", valueExpr);
    return pathExpr.like(patternExpr);
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

  private static Optional<Predicate> buildOrPredicate(Stream<Supplier<Optional<Predicate>>> expressions) {
    final Collection<Predicate> predicates = expressions
        .map(Supplier::get)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();

    if (predicates.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(ExpressionUtils.anyOf(predicates));
  }
}
