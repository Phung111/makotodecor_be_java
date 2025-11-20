package com.makotodecor.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.makotodecor.model.entity.Product;
import com.makotodecor.model.entity.QProduct;
import com.makotodecor.model.entity.QSize;
import com.makotodecor.util.PaginationUtils;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Product> findAllWithSpecialSort(Predicate predicate, Pageable pageable) {
    QProduct qProduct = QProduct.product;
    Sort sort = pageable.getSort();

    boolean hasPriceSort = sort.stream()
        .anyMatch(order -> "price".equals(order.getProperty()));

    JPAQuery<Product> query = queryFactory
        .selectFrom(qProduct)
        .where(predicate);

    Sort transformedSort = PaginationUtils.transformSortForProduct(sort);
    Pageable transformedPageable = PageRequest.of(
        pageable.getPageNumber(),
        pageable.getPageSize(),
        transformedSort);

    if (hasPriceSort) {
      QSize qSize = QSize.size1;
      Expression<Long> minPriceSubquery = JPAExpressions
          .select(qSize.price.min())
          .from(qSize)
          .where(qSize.product().id.eq(qProduct.id));

      List<OrderSpecifier<?>> specialOrderSpecifiers = PaginationUtils.buildProductOrderSpecifiers(sort, qProduct,
          minPriceSubquery);
      if (!specialOrderSpecifiers.isEmpty()) {
        query.orderBy(specialOrderSpecifiers.toArray(new OrderSpecifier[0]));
      }
    } else {
      if (!transformedSort.isUnsorted()) {
        for (Sort.Order order : transformedSort) {
          String property = order.getProperty();
          com.querydsl.core.types.Order direction = order.getDirection().isAscending()
              ? com.querydsl.core.types.Order.ASC
              : com.querydsl.core.types.Order.DESC;

          com.querydsl.core.types.Path<?> path = getPathForProperty(property, qProduct);
          @SuppressWarnings({ "unchecked", "rawtypes" })
          OrderSpecifier<?> specifier = new OrderSpecifier(direction, path);
          query.orderBy(specifier);
        }
      }
    }

    long total = queryFactory
        .select(qProduct.count())
        .from(qProduct)
        .where(predicate)
        .fetchOne();

    List<Product> content = query
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    return new PageImpl<>(content, transformedPageable, total);
  }

  private com.querydsl.core.types.Path<?> getPathForProperty(String property, QProduct qProduct) {
    return switch (property) {
      case "id" -> qProduct.id;
      case "name" -> qProduct.name;
      case "status" -> qProduct.status;
      case "discount" -> qProduct.discount;
      case "sold" -> qProduct.sold;
      case "updatedAt" -> qProduct.updatedAt;
      case "category.name" -> qProduct.category().name;
      default -> qProduct.id;
    };
  }
}
