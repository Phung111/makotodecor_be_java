package com.makotodecor.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.makotodecor.model.CartProductGroupResponse;
import com.makotodecor.model.CartResponse;
import com.makotodecor.model.CartVariantResponse;
import com.makotodecor.model.entity.Cart;
import com.makotodecor.model.entity.CartItem;
import com.makotodecor.model.entity.Color;
import com.makotodecor.model.entity.Product;
import com.makotodecor.model.entity.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.openapitools.jackson.nullable.JsonNullable;

@Mapper(componentModel = "spring", uses = { ProductMapper.class })
public interface CartMapper {

  @Mapping(target = "itemCount", ignore = true)
  @Mapping(target = "groupedCartItems", ignore = true)
  CartResponse toCartResponse(Cart cart);

  @Mapping(target = "productId", source = "product.id")
  @Mapping(target = "productName", source = "product.name")
  @Mapping(target = "defaultImage", expression = "java(getDefaultImage(product))")
  @Mapping(target = "variants", expression = "java(mapCartItemsToVariants(cartItems))")
  CartProductGroupResponse toCartProductGroupResponse(Product product, List<CartItem> cartItems);

  default CartResponse mapCartToResponse(Cart cart) {
    if (cart == null || cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
      return CartResponse.builder()
          .itemCount(0L)
          .groupedCartItems(new ArrayList<>())
          .build();
    }

    // Group cart items by product
    Map<Product, List<CartItem>> groupedByProduct = cart.getCartItems().stream()
        .filter(item -> item.getProduct() != null)
        .collect(Collectors.groupingBy(CartItem::getProduct));

    List<CartProductGroupResponse> groupedCartItems = groupedByProduct.entrySet().stream()
        .map(entry -> toCartProductGroupResponse(entry.getKey(), entry.getValue()))
        .toList();

    // Calculate item count as number of variants (cart items)
    Long itemCount = (long) cart.getCartItems().size();

    return CartResponse.builder()
        .itemCount(itemCount)
        .groupedCartItems(groupedCartItems)
        .build();
  }

  default List<CartVariantResponse> mapCartItemsToVariants(List<CartItem> cartItems) {
    if (cartItems == null) {
      return new ArrayList<>();
    }

    Map<String, List<CartItem>> groupedByVariant = cartItems.stream()
        .filter(item -> item.getSize() != null)
        .collect(Collectors.groupingBy(item -> buildVariantKey(
            item.getSize() != null ? item.getSize().getId() : null,
            item.getColor() != null ? item.getColor().getId() : null
        )));

    return groupedByVariant.values().stream()
        .map(group -> {
          CartItem first = group.get(0);
          Size size = first.getSize();
          Color color = first.getColor();

          long totalQuantity = group.stream()
              .mapToLong(ci -> ci.getQuantity() != null ? ci.getQuantity() : 0L)
              .sum();

          CartVariantResponse.CartVariantResponseBuilder builder = CartVariantResponse.builder()
              .id(first.getId())
              .priceId(size != null ? size.getId() : null)
              .size(size != null ? size.getSize() : null)
              .price(first.getPrice())
              .discount(first.getDiscount())
              .finalPrice(calculateFinalPrice(first.getPrice(), first.getDiscount()))
              .quantity(totalQuantity);

          if (color != null) {
            builder
                .colorId(JsonNullable.of(color.getId()))
                .colorName(color.getName())
                .colorCode(color.getColor())
                .colorImage(getColorImage(color));
          }

          return builder.build();
        })
        .filter(Objects::nonNull)
        .toList();
  }

  default Long calculateFinalPrice(Long price, Long discount) {
    if (price == null) {
      return 0L;
    }
    Long discountValue = discount != null ? discount : 0L;
    return price * (100 - discountValue) / 100;
  }

  default com.makotodecor.model.ImageInfo getDefaultImage(Product product) {
    if (product == null || product.getImgs() == null) {
      return null;
    }
    return product.getImgs().stream()
        .filter(img -> Boolean.TRUE.equals(img.getIsDefault()))
        .findFirst()
        .map(img -> {
          com.makotodecor.model.ImageInfo imageInfo = new com.makotodecor.model.ImageInfo();
          imageInfo.setUrl(img.getUrl());
          imageInfo.setPublicId(img.getPublicId());
          if (img.getImgType() != null) {
            imageInfo.setImgTypeId(img.getImgType().getId());
            imageInfo.setTypeCode(img.getImgType().getCode());
          }
          return imageInfo;
        })
        .orElse(null);
  }

  default com.makotodecor.model.ImageInfo getColorImage(Color color) {
    if (color == null || color.getImg() == null) {
      return null;
    }
    com.makotodecor.model.ImageInfo imageInfo = new com.makotodecor.model.ImageInfo();
    imageInfo.setUrl(color.getImg().getUrl());
    imageInfo.setPublicId(color.getImg().getPublicId());
    if (color.getImg().getImgType() != null) {
      imageInfo.setImgTypeId(color.getImg().getImgType().getId());
      imageInfo.setTypeCode(color.getImg().getImgType().getCode());
    }
    return imageInfo;
  }

  private String buildVariantKey(Long sizeId, Long colorId) {
    return (sizeId != null ? sizeId : "null") + "-" + (colorId != null ? colorId : "null");
  }
}
