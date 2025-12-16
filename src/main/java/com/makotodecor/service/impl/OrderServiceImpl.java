package com.makotodecor.service.impl;

import com.makotodecor.exceptions.WebBadRequestException;
import com.makotodecor.exceptions.base.ErrorMessage;
import com.makotodecor.mapper.OrderMapper;
import com.makotodecor.model.CreateOrderRequest;
import com.makotodecor.model.OrderDetailResponse;
import com.makotodecor.model.OrdersPagedResponse;
import com.makotodecor.model.UpdateOrderStatusRequest;
import com.makotodecor.model.dto.OrderPagedCriteria;
import com.makotodecor.model.entity.Order;
import com.makotodecor.model.entity.OrderGroup;
import com.makotodecor.model.entity.OrderItem;
import com.makotodecor.model.entity.Product;
import com.makotodecor.model.entity.User;
import com.makotodecor.model.enums.OrderStatusEnum;
import com.makotodecor.repository.OrderGroupRepository;
import com.makotodecor.repository.OrderItemRepository;
import com.makotodecor.repository.OrderRepository;
import com.makotodecor.repository.ProductRepository;
import com.makotodecor.repository.UserRepository;
import com.makotodecor.service.OrderService;
import com.makotodecor.util.PaginationUtils;
import com.makotodecor.util.QuerydslCriteriaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final OrderGroupRepository orderGroupRepository;
  private final OrderItemRepository orderItemRepository;

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

  @Override
  @Transactional
  public OrderDetailResponse placeOrder(CreateOrderRequest request, String username) {
    User user = findUserByUsername(username);

    // Create base order
    final Order order = Order.builder()
        .code(generateOrderCode())
        .status(OrderStatusEnum.NEW)
        .user(user)
        .shippingFullName(request.getShippingInfo() != null ? request.getShippingInfo().getFullName() : null)
        .shippingPhone(request.getShippingInfo() != null ? request.getShippingInfo().getPhone() : null)
        .shippingAddress(request.getShippingInfo() != null ? request.getShippingInfo().getAddress() : null)
        .shippingNote(request.getShippingInfo() != null ? request.getShippingInfo().getNote() : null)
        .paymentProofUrl(request.getPaymentProof() != null ? request.getPaymentProof().getUrl() : null)
        .createdAt(ZonedDateTime.now())
        .build();

    orderRepository.save(order);

    if (request.getCheckoutItems() == null || request.getCheckoutItems().isEmpty()) {
      throw new WebBadRequestException(ErrorMessage.ITEM_NOT_FOUND);
    }

    // Group checkout items by productId
    Map<Long, List<com.makotodecor.model.CreateOrderCheckoutItem>> groupedByProduct = request.getCheckoutItems().stream()
        .collect(Collectors.groupingBy(com.makotodecor.model.CreateOrderCheckoutItem::getProductId));

    List<OrderGroup> orderGroups = new ArrayList<>();
    List<OrderItem> orderItems = new ArrayList<>();

    groupedByProduct.forEach((productId, items) -> {
      Product product = productRepository.findById(productId)
          .orElseThrow(() -> new WebBadRequestException(ErrorMessage.PRODUCT_NOT_FOUND));

      com.makotodecor.model.CreateOrderCheckoutItem first = items.get(0);

      String productImagesJson = null;
      try {
        if (first.getProductImages() != null && !first.getProductImages().isEmpty()) {
          productImagesJson = com.fasterxml.jackson.databind.json.JsonMapper.builder().build()
              .writeValueAsString(first.getProductImages());
        }
      } catch (Exception e) {
        // ignore serialization error, keep null
      }

      OrderGroup group = OrderGroup.builder()
          .order(order)
          .product(product)
          .productName(first.getProductName())
          .productImages(productImagesJson)
          .createdAt(ZonedDateTime.now())
          .build();
      orderGroups.add(group);

      items.forEach(ci -> {
        com.makotodecor.model.CreateOrderVariant v = ci.getVariant();
        if (v == null) {
          return;
        }

        String variantImagesJson = null;
        try {
          if (v.getVariantImages() != null && !v.getVariantImages().isEmpty()) {
            variantImagesJson = com.fasterxml.jackson.databind.json.JsonMapper.builder().build()
                .writeValueAsString(v.getVariantImages());
          }
        } catch (Exception e) {
          // ignore
        }

        OrderItem orderItem = OrderItem.builder()
            .order(order)
            .orderGroup(group)
            .product(product)
            .quantity(v.getQuantity() != null ? v.getQuantity() : 1L)
            .price(v.getPrice())
            .discount(v.getDiscount() != null ? v.getDiscount() : 0L)
            .colorName(v.getColorName())
            .sizeName(v.getSize())
            .sizePrice(v.getPrice())
            .variantImages(variantImagesJson)
            .build();
        orderItems.add(orderItem);
      });
    });

    orderGroupRepository.saveAll(orderGroups);
    orderItemRepository.saveAll(orderItems);

    // Calculate total from items to avoid trusting client
    Long total = orderItems.stream()
        .map(item -> orderMapper.calculateSubtotal(item))
        .reduce(0L, Long::sum);
    order.setTotalPrice(total);
    orderRepository.save(order);

    return orderMapper.toOrderDetailResponse(order);
  }

  private Order findOrderById(Long orderId) {
    return orderRepository.findById(orderId)
        .orElseThrow(() -> new WebBadRequestException(ErrorMessage.ORDER_NOT_FOUND));
  }

  private User findUserByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new org.springframework.security.authentication.AuthenticationCredentialsNotFoundException("User not found"));
  }

  private String generateOrderCode() {
    return "ORD-" + UUID.randomUUID();
  }
}
