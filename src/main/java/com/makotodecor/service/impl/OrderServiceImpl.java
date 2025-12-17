package com.makotodecor.service.impl;

import com.makotodecor.exceptions.WebBadRequestException;
import com.makotodecor.exceptions.base.ErrorMessage;
import com.makotodecor.mapper.OrderMapper;
import com.makotodecor.model.CreateOrderRequest;
import com.makotodecor.model.OrderDetailResponse;
import com.makotodecor.model.OrdersPagedResponse;
import com.makotodecor.model.UpdateOrderStatusRequest;
import com.makotodecor.model.dto.OrderPagedCriteria;
import com.makotodecor.model.entity.Img;
import com.makotodecor.model.entity.ImgType;
import com.makotodecor.model.entity.Order;
import com.makotodecor.model.entity.OrderGroup;
import com.makotodecor.model.entity.OrderItem;
import com.makotodecor.model.entity.Product;
import com.makotodecor.model.entity.User;
import com.makotodecor.model.enums.OrderStatusEnum;
import com.makotodecor.repository.ImgRepository;
import com.makotodecor.repository.ImgTypeRepository;
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
import java.util.Set;
import java.util.UUID;

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
  private final ImgRepository imgRepository;
  private final ImgTypeRepository imgTypeRepository;

  private static final String ORDER_GROUP_IMG_TYPE_CODE = "ORDER_GROUP";
  private static final String ORDER_ITEM_IMG_TYPE_CODE = "ORDER_ITEM";

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

    // Fetch image types for ORDER_GROUP and ORDER_ITEM
    ImgType orderGroupImgType = imgTypeRepository.findByCode(ORDER_GROUP_IMG_TYPE_CODE)
        .orElseThrow(() -> new WebBadRequestException(ErrorMessage.ITEM_NOT_FOUND));
    ImgType orderItemImgType = imgTypeRepository.findByCode(ORDER_ITEM_IMG_TYPE_CODE)
        .orElseThrow(() -> new WebBadRequestException(ErrorMessage.ITEM_NOT_FOUND));

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

    if (request.getOrderGroups() == null || request.getOrderGroups().isEmpty()) {
      throw new WebBadRequestException(ErrorMessage.ITEM_NOT_FOUND);
    }

    List<OrderGroup> orderGroups = new ArrayList<>();
    List<OrderItem> orderItems = new ArrayList<>();
    List<Img> allImages = new ArrayList<>();

    for (com.makotodecor.model.CreateOrderGroup reqGroup : request.getOrderGroups()) {
      Product product = productRepository.findById(reqGroup.getProductId())
          .orElseThrow(() -> new WebBadRequestException(ErrorMessage.PRODUCT_NOT_FOUND));

      OrderGroup group = OrderGroup.builder()
          .order(order)
          .product(product)
          .productName(reqGroup.getProductName())
          .createdAt(ZonedDateTime.now())
          .build();
      orderGroups.add(group);

      // Save order group first to get ID
      orderGroupRepository.save(group);

      // Create Img entities for productImages with type ORDER_GROUP
      if (reqGroup.getProductImages() != null && !reqGroup.getProductImages().isEmpty()) {
        long priority = 1;
        for (com.makotodecor.model.ImageInfo imgInfo : reqGroup.getProductImages()) {
          Img img = Img.builder()
              .url(imgInfo.getUrl())
              .publicId(imgInfo.getPublicId())
              .priority(priority++)
              .isDefault(priority == 2) // First image is default
              .imgType(orderGroupImgType)
              .orderGroup(group)
              .createdAt(ZonedDateTime.now())
              .build();
          allImages.add(img);
        }
      }

      // Process order items for this group
      if (reqGroup.getOrderItems() != null) {
        for (com.makotodecor.model.CreateOrderItem reqItem : reqGroup.getOrderItems()) {
          OrderItem orderItem = OrderItem.builder()
              .order(order)
              .orderGroup(group)
              .product(product)
              .quantity(reqItem.getQuantity() != null ? reqItem.getQuantity() : 1L)
              .price(reqItem.getPrice())
              .discount(reqItem.getDiscount() != null ? reqItem.getDiscount() : 0L)
              .colorName(reqItem.getColorName())
              .sizeName(reqItem.getSizeName())
              .sizePrice(reqItem.getPrice())
              .build();
          orderItems.add(orderItem);

          // Save order item first to get ID
          orderItemRepository.save(orderItem);

          // Create Img entities for variantImages with type ORDER_ITEM
          if (reqItem.getVariantImages() != null && !reqItem.getVariantImages().isEmpty()) {
            long priority = 1;
            for (com.makotodecor.model.ImageInfo imgInfo : reqItem.getVariantImages()) {
              Img img = Img.builder()
                  .url(imgInfo.getUrl())
                  .publicId(imgInfo.getPublicId())
                  .priority(priority++)
                  .isDefault(priority == 2) // First image is default
                  .imgType(orderItemImgType)
                  .orderItem(orderItem)
                  .createdAt(ZonedDateTime.now())
                  .build();
              allImages.add(img);
            }
          }
        }
      }
    }

    // Save all images
    if (!allImages.isEmpty()) {
      imgRepository.saveAll(allImages);
    }

    // Calculate total from items to avoid trusting client
    Long total = orderItems.stream()
        .map(item -> orderMapper.calculateSubtotal(item))
        .reduce(0L, Long::sum);
    order.setTotalPrice(total);
    orderRepository.save(order);

    return orderMapper.toOrderDetailResponse(order);
  }

  @Override
  @Transactional(readOnly = true)
  public OrdersPagedResponse getMyOrdersPaged(OrderPagedCriteria criteria, String username) {
    User user = findUserByUsername(username);

    var sortCriteria = PaginationUtils.parseSortCriteria(criteria.getOrderBy());
    PaginationUtils.validateSortColumns(sortCriteria, ORDER_SORTABLE_COLUMNS);

    var pageable = PageRequest
        .of(criteria.getPage(), criteria.getSize())
        .withSort(sortCriteria);

    // Build predicate with user filter
    var criteriaWithUser = OrderPagedCriteria.builder()
        .page(criteria.getPage())
        .size(criteria.getSize())
        .orderBy(criteria.getOrderBy())
        .status(criteria.getStatus())
        .userId(user.getId())
        .build();

    var predicate = QuerydslCriteriaUtils.buildOrderSearchPredicate(criteriaWithUser)
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
  public OrderDetailResponse getMyOrder(Long orderId, String username) {
    User user = findUserByUsername(username);
    Order order = findOrderById(orderId);

    // Verify the order belongs to the user
    if (!order.getUser().getId().equals(user.getId())) {
      throw new WebBadRequestException(ErrorMessage.ORDER_NOT_FOUND);
    }

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
