package com.makotodecor.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.makotodecor.model.OrderDetailResponse;
import com.makotodecor.model.OrderItemDetailResponse;
import com.makotodecor.model.OrderItemResponse;
import com.makotodecor.model.entity.Order;
import com.makotodecor.model.entity.OrderItem;

import java.util.List;

@Mapper(componentModel = "spring", uses = { ProductMapper.class, UserMapper.class })
public interface OrderMapper {

  @Mapping(target = "status", expression = "java(mapStatusToDto(order.getStatus()))")
  @Mapping(target = "userId", source = "user.id")
  @Mapping(target = "username", source = "user.username")
  @Mapping(target = "userEmail", source = "user.email")
  @Mapping(target = "total", expression = "java(calculateTotal(order))")
  @Mapping(target = "itemCount", expression = "java(getItemCount(order))")
  OrderItemResponse toOrderItemResponse(Order order);

  @Mapping(target = "status", expression = "java(mapStatusToDto(order.getStatus()))")
  @Mapping(target = "user", source = "order.user")
  @Mapping(target = "orderItems", expression = "java(mapOrderItems(order.getOrderItems()))")
  @Mapping(target = "total", expression = "java(calculateTotal(order))")
  OrderDetailResponse toOrderDetailResponse(Order order);

  @Mapping(target = "product", source = "orderItem.product")
  @Mapping(target = "subtotal", expression = "java(calculateSubtotal(orderItem))")
  OrderItemDetailResponse toOrderItemDetailResponse(OrderItem orderItem);

  default List<OrderItemDetailResponse> mapOrderItems(List<OrderItem> orderItems) {
    if (orderItems == null) {
      return List.of();
    }
    return orderItems.stream()
        .map(this::toOrderItemDetailResponse)
        .toList();
  }

  default Long calculateTotal(Order order) {
    if (order == null || order.getOrderItems() == null) {
      return 0L;
    }
    return order.getOrderItems().stream()
        .map(this::calculateSubtotal)
        .reduce(0L, Long::sum);
  }

  default Long calculateSubtotal(OrderItem orderItem) {
    if (orderItem == null) {
      return 0L;
    }
    Long quantity = orderItem.getQuantity() != null ? orderItem.getQuantity() : 0L;
    Long price = orderItem.getPrice() != null ? orderItem.getPrice() : 0L;
    Long discount = orderItem.getDiscount() != null ? orderItem.getDiscount() : 0L;
    Long sizePrice = orderItem.getSizePrice() != null ? orderItem.getSizePrice() : 0L;

    Long finalPrice = price + sizePrice;
    Long finalAmount = finalPrice * (100 - discount) / 100;
    return finalAmount * quantity;
  }

  default Long getItemCount(Order order) {
    if (order == null || order.getOrderItems() == null) {
      return 0L;
    }
    return order.getOrderItems().stream()
        .mapToLong(item -> item.getQuantity() != null ? item.getQuantity() : 0L)
        .sum();
  }

  default com.makotodecor.model.OrderStatusEnum mapStatusToDto(
      com.makotodecor.model.enums.OrderStatusEnum status) {
    if (status == null) {
      return null;
    }
    return com.makotodecor.model.OrderStatusEnum.valueOf(status.name());
  }
}
