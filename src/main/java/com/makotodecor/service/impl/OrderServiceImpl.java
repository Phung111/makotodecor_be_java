package com.makotodecor.service.impl;

import com.makotodecor.exceptions.WebBadRequestException;
import com.makotodecor.exceptions.base.ErrorMessage;
import com.makotodecor.mapper.OrderMapper;
import com.makotodecor.model.OrderDetailResponse;
import com.makotodecor.model.OrdersPagedResponse;
import com.makotodecor.model.UpdateOrderStatusRequest;
import com.makotodecor.model.dto.OrderPagedCriteria;
import com.makotodecor.model.entity.Order;
import com.makotodecor.model.enums.OrderStatusEnum;
import com.makotodecor.repository.OrderRepository;
import com.makotodecor.service.OrderService;
import com.makotodecor.util.PaginationUtils;
import com.makotodecor.util.QuerydslCriteriaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;

  private static final Set<String> ORDER_SORTABLE_COLUMNS = Set.of("id", "status", "createdAt", "updatedAt");

  @Override
  @Transactional(readOnly = true)
  public OrdersPagedResponse getOrdersPaged(OrderPagedCriteria criteria) {
    var sortCriteria = PaginationUtils.parseSortCriteria(criteria.getOrderBy());
    PaginationUtils.validateSortColumns(sortCriteria, ORDER_SORTABLE_COLUMNS);

    var pageable = PageRequest
        .of(criteria.getPage(), criteria.getSize())
        .withSort(sortCriteria);

    var predicate = QuerydslCriteriaUtils.buildOrderSearchPredicate(criteria)
        .orElse(QuerydslCriteriaUtils.truePredicate());

    var pageResponse = orderRepository.findAll(predicate, pageable);

    // Eager load orderItems and user to avoid lazy loading issues
    var orders = pageResponse.getContent();
    orders.forEach(order -> {
      if (order.getOrderItems() != null) {
        order.getOrderItems().size();
        order.getOrderItems().forEach(item -> {
          if (item.getProduct() != null) {
            item.getProduct().getName();
          }
        });
      }
      if (order.getUser() != null) {
        order.getUser().getUsername();
      }
    });

    return OrdersPagedResponse.builder()
        .pageInfo(PaginationUtils.toPageInfo(pageResponse))
        .items(orders.stream()
            .map(orderMapper::toOrderItemResponse)
            .toList())
        .build();
  }

  @Override
  @Transactional(readOnly = true)
  public OrderDetailResponse getOrder(Long orderId) {
    Order order = findOrderById(orderId);

    // Eager load orderItems and user
    if (order.getOrderItems() != null) {
      order.getOrderItems().size();
      order.getOrderItems().forEach(item -> {
        if (item.getProduct() != null) {
          item.getProduct().getName();
          if (item.getProduct().getCategory() != null) {
            item.getProduct().getCategory().getName();
          }
        }
      });
    }
    if (order.getUser() != null) {
      order.getUser().getUsername();
    }

    return orderMapper.toOrderDetailResponse(order);
  }

  @Override
  @Transactional
  public void updateOrderStatus(UpdateOrderStatusRequest request) {
    Order order = findOrderById(request.getOrderId());

    var newStatus = OrderStatusEnum.valueOf(request.getStatus().getValue());
    order.setStatus(newStatus);

    orderRepository.save(order);
  }

  private Order findOrderById(Long orderId) {
    return orderRepository.findById(orderId)
        .orElseThrow(() -> new WebBadRequestException(ErrorMessage.ORDER_NOT_FOUND));
  }
}
