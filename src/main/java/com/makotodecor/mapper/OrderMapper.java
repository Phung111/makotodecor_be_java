package com.makotodecor.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.makotodecor.model.CreateOrderShippingInfo;
import com.makotodecor.model.ImageInfo;
import com.makotodecor.model.OrderDetailResponse;
import com.makotodecor.model.OrderGroupResponse;
import com.makotodecor.model.OrderItemDetailResponse;
import com.makotodecor.model.OrderItemResponse;
import com.makotodecor.model.entity.Img;
import com.makotodecor.model.entity.Order;
import com.makotodecor.model.entity.OrderGroup;
import com.makotodecor.model.entity.OrderItem;

import java.util.List;

@Mapper(componentModel = "spring", uses = { ProductMapper.class, UserMapper.class })
public interface OrderMapper {

  // Map Order to OrderItemResponse (for list view)
  @Mapping(target = "status", expression = "java(mapStatusToDto(order.getStatus()))")
  @Mapping(target = "userId", source = "user.id")
  @Mapping(target = "username", source = "user.username")
  @Mapping(target = "userEmail", source = "user.email")
  @Mapping(target = "total", source = "totalPrice")
  @Mapping(target = "itemCount", expression = "java(getItemCount(order))")
  OrderItemResponse toOrderItemResponse(Order order);

  // Map Order to OrderDetailResponse (for detail view)
  @Mapping(target = "status", expression = "java(mapStatusToDto(order.getStatus()))")
  @Mapping(target = "user", source = "order.user")
  @Mapping(target = "shippingInfo", expression = "java(mapShippingInfo(order))")
  @Mapping(target = "orderGroups", expression = "java(mapOrderGroups(order.getOrderGroups()))")
  @Mapping(target = "paymentProof", expression = "java(mapPaymentProof(order))")
  @Mapping(target = "totalPrice", source = "totalPrice")
  @Mapping(target = "total", source = "totalPrice")
  OrderDetailResponse toOrderDetailResponse(Order order);

  // Map OrderGroup to OrderGroupResponse
  @Mapping(target = "id", source = "orderGroup.id")
  @Mapping(target = "productId", source = "orderGroup.product.id")
  @Mapping(target = "productName", source = "orderGroup.productName")
  @Mapping(target = "productImages", expression = "java(mapProductImages(orderGroup.getProductImages()))")
  @Mapping(target = "orderItems", expression = "java(mapOrderItems(orderGroup.getOrderItems()))")
  OrderGroupResponse toOrderGroupResponse(OrderGroup orderGroup);

  // Map OrderItem to OrderItemDetailResponse
  @Mapping(target = "id", source = "orderItem.id")
  @Mapping(target = "cartItemId", ignore = true)
  @Mapping(target = "sizeId", ignore = true)
  @Mapping(target = "sizeName", source = "orderItem.sizeName")
  @Mapping(target = "colorId", ignore = true)
  @Mapping(target = "colorName", source = "orderItem.colorName")
  @Mapping(target = "colorCode", ignore = true)
  @Mapping(target = "priceId", ignore = true)
  @Mapping(target = "price", source = "orderItem.price")
  @Mapping(target = "discount", source = "orderItem.discount")
  @Mapping(target = "finalPrice", expression = "java(calculateFinalPrice(orderItem))")
  @Mapping(target = "quantity", source = "orderItem.quantity")
  @Mapping(target = "subtotal", expression = "java(calculateSubtotal(orderItem))")
  @Mapping(target = "variantImages", expression = "java(mapVariantImages(orderItem.getVariantImages()))")
  OrderItemDetailResponse toOrderItemDetailResponse(OrderItem orderItem);

  // Map shipping info from Order
  default CreateOrderShippingInfo mapShippingInfo(Order order) {
    if (order == null) {
      return null;
    }
    CreateOrderShippingInfo shippingInfo = new CreateOrderShippingInfo();
    shippingInfo.setFullName(order.getShippingFullName());
    shippingInfo.setPhone(order.getShippingPhone());
    shippingInfo.setAddress(order.getShippingAddress());
    shippingInfo.setNote(order.getShippingNote());
    return shippingInfo;
  }

  // Map payment proof from Order
  default ImageInfo mapPaymentProof(Order order) {
    if (order == null || order.getPaymentProofUrl() == null) {
      return null;
    }
    ImageInfo imageInfo = new ImageInfo();
    imageInfo.setUrl(order.getPaymentProofUrl());
    imageInfo.setPublicId(order.getPaymentProofPublicId());
    return imageInfo;
  }

  // Map list of OrderGroups
  default List<OrderGroupResponse> mapOrderGroups(List<OrderGroup> orderGroups) {
    if (orderGroups == null) {
      return List.of();
    }
    return orderGroups.stream()
        .map(this::toOrderGroupResponse)
        .toList();
  }

  // Map list of OrderItems
  default List<OrderItemDetailResponse> mapOrderItems(List<OrderItem> orderItems) {
    if (orderItems == null) {
      return List.of();
    }
    return orderItems.stream()
        .map(this::toOrderItemDetailResponse)
        .toList();
  }

  // Map product images from Img entities
  default List<ImageInfo> mapProductImages(List<Img> images) {
    if (images == null) {
      return List.of();
    }
    return images.stream()
        .map(img -> {
          ImageInfo imageInfo = new ImageInfo();
          imageInfo.setUrl(img.getUrl());
          imageInfo.setPublicId(img.getPublicId());
          return imageInfo;
        })
        .toList();
  }

  // Map variant images from Img entities
  default List<ImageInfo> mapVariantImages(List<Img> images) {
    if (images == null) {
      return List.of();
    }
    return images.stream()
        .map(img -> {
          ImageInfo imageInfo = new ImageInfo();
          imageInfo.setUrl(img.getUrl());
          imageInfo.setPublicId(img.getPublicId());
          return imageInfo;
        })
        .toList();
  }

  // Calculate final price after discount
  default Long calculateFinalPrice(OrderItem orderItem) {
    if (orderItem == null) {
      return 0L;
    }
    Long price = orderItem.getPrice() != null ? orderItem.getPrice() : 0L;
    Long discount = orderItem.getDiscount() != null ? orderItem.getDiscount() : 0L;
    Long sizePrice = orderItem.getSizePrice() != null ? orderItem.getSizePrice() : 0L;
    
    Long totalPrice = price + sizePrice;
    return totalPrice * (100 - discount) / 100;
  }

  // Calculate subtotal for an order item
  default Long calculateSubtotal(OrderItem orderItem) {
    if (orderItem == null) {
      return 0L;
    }
    Long quantity = orderItem.getQuantity() != null ? orderItem.getQuantity() : 0L;
    Long finalPrice = calculateFinalPrice(orderItem);
    return finalPrice * quantity;
  }

  // Get total item count from order
  default Long getItemCount(Order order) {
    if (order == null || order.getOrderGroups() == null) {
      return 0L;
    }
    return order.getOrderGroups().stream()
        .filter(group -> group.getOrderItems() != null)
        .flatMap(group -> group.getOrderItems().stream())
        .mapToLong(item -> item.getQuantity() != null ? item.getQuantity() : 0L)
        .sum();
  }

  // Map status enum to DTO enum
  default com.makotodecor.model.OrderStatusEnum mapStatusToDto(
      com.makotodecor.model.enums.OrderStatusEnum status) {
    if (status == null) {
      return null;
    }
    return com.makotodecor.model.OrderStatusEnum.valueOf(status.name());
  }
}
