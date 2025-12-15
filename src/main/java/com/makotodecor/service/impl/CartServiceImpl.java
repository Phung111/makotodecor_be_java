package com.makotodecor.service.impl;

import com.makotodecor.exceptions.WebBadRequestException;
import com.makotodecor.exceptions.base.ErrorMessage;
import com.makotodecor.mapper.CartMapper;
import com.makotodecor.util.CartValidateUtils;
import com.makotodecor.model.AddCartItemRequest;
import com.makotodecor.model.CartItemIdRequest;
import com.makotodecor.model.CartCountResponse;
import com.makotodecor.model.CartResponse;
import com.makotodecor.model.ChangeQuantityCartItemRequest;
import com.makotodecor.model.entity.Cart;
import com.makotodecor.model.entity.CartItem;
import com.makotodecor.model.entity.Color;
import com.makotodecor.model.entity.Product;
import com.makotodecor.model.entity.Size;
import com.makotodecor.model.entity.User;
import com.makotodecor.repository.CartItemRepository;
import com.makotodecor.repository.CartRepository;
import com.makotodecor.repository.ColorRepository;
import com.makotodecor.repository.ProductRepository;
import com.makotodecor.repository.SizeRepository;
import com.makotodecor.repository.UserRepository;
import com.makotodecor.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final SizeRepository sizeRepository;
  private final ColorRepository colorRepository;
  private final CartMapper cartMapper;

  @Override
  public CartResponse getCart(String username) {
    User user = findUserByUsername(username);
    Cart cart = findCart(user);

    if (cart == null) {
      return cartMapper.mapCartToResponse(null);
    }

    if (cart.getCartItems() != null) {
      cart.getCartItems().size();
      cart.getCartItems().forEach(item -> {
        if (item.getProduct() != null) {
          item.getProduct().getName();
          if (item.getProduct().getImgs() != null) {
            item.getProduct().getImgs().size();
          }
        }
        if (item.getSize() != null) {
          item.getSize().getSize();
        }
        if (item.getColor() != null) {
          item.getColor().getName();
          if (item.getColor().getImg() != null) {
            item.getColor().getImg().getUrl();
          }
        }
      });
    }

    return cartMapper.mapCartToResponse(cart);
  }

  @Override
  public CartCountResponse getCartCount(String username) {
    User user = findUserByUsername(username);
    Cart cart = findCart(user);

    long itemCount = 0;
    if (cart != null && cart.getCartItems() != null) {
      itemCount = cart.getCartItems().size();
    }

    CartCountResponse response = new CartCountResponse();
    response.setItemCount(itemCount);
    return response;
  }

  @Override
  @Transactional
  public CartResponse addCartItem(String username, AddCartItemRequest request) {
    CartValidateUtils.validateAddCartItemRequest(request);

    User user = findUserByUsername(username);
    Cart cart = findOrCreateCart(user);

    Product product = productRepository.findById(request.getProductId())
        .orElseThrow(() -> new WebBadRequestException(ErrorMessage.PRODUCT_NOT_FOUND));

    Size size = sizeRepository.findById(request.getSizeId())
        .orElseThrow(() -> new WebBadRequestException(ErrorMessage.PRODUCT_NOT_FOUND));

    Color color = null;
    if (request.getColorId() != null && request.getColorId().isPresent() && request.getColorId().get() != null) {
      color = colorRepository.findById(request.getColorId().get())
          .orElseThrow(() -> new WebBadRequestException(ErrorMessage.PRODUCT_NOT_FOUND));
    }

    Optional<CartItem> existingCartItem = cartItemRepository.findByCartAndProductAndSizeAndColor(
        cart, product, size, color);

    if (existingCartItem.isPresent()) {
      CartItem cartItem = existingCartItem.get();
      cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
      cartItem.setUpdatedAt(ZonedDateTime.now());
      cartItemRepository.save(cartItem);
    } else {
      Long price = size.getPrice();
      Long discount = product.getDiscount() != null ? product.getDiscount() : 0L;

      CartItem cartItem = CartItem.builder()
          .cart(cart)
          .product(product)
          .size(size)
          .color(color)
          .quantity(request.getQuantity())
          .price(price)
          .discount(discount)
          .createdAt(ZonedDateTime.now())
          .updatedAt(ZonedDateTime.now())
          .build();

      cartItemRepository.save(cartItem);
    }

    return getCart(username);
  }

  @Override
  @Transactional
  public CartResponse increaseCartItem(String username, CartItemIdRequest request) {
    CartItem cartItem = findCartItemById(request.getCartItemId());
    validateCartItemOwnership(cartItem, username);

    cartItem.setQuantity(cartItem.getQuantity() + 1);
    cartItem.setUpdatedAt(ZonedDateTime.now());
    cartItemRepository.save(cartItem);

    return getCart(username);
  }

  @Override
  @Transactional
  public CartResponse decreaseCartItem(String username, CartItemIdRequest request) {
    CartItem cartItem = findCartItemById(request.getCartItemId());
    validateCartItemOwnership(cartItem, username);

    if (cartItem.getQuantity() <= 1) {
      cartItemRepository.delete(cartItem);
    } else {
      cartItem.setQuantity(cartItem.getQuantity() - 1);
      cartItem.setUpdatedAt(ZonedDateTime.now());
      cartItemRepository.save(cartItem);
    }

    return getCart(username);
  }

  @Override
  @Transactional
  public CartResponse changeQuantityCartItem(String username, ChangeQuantityCartItemRequest request) {
    CartValidateUtils.validateChangeQuantityCartItemRequest(request);

    CartItem cartItem = findCartItemById(request.getCartItemId());
    validateCartItemOwnership(cartItem, username);

    cartItem.setQuantity(request.getQuantity());
    cartItem.setUpdatedAt(ZonedDateTime.now());
    cartItemRepository.save(cartItem);

    return getCart(username);
  }

  @Override
  @Transactional
  public CartResponse deleteCartItem(String username, CartItemIdRequest request) {
    CartItem cartItem = findCartItemById(request.getCartItemId());
    validateCartItemOwnership(cartItem, username);

    cartItemRepository.delete(cartItem);

    return getCart(username);
  }

  private User findUserByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new org.springframework.security.authentication.AuthenticationCredentialsNotFoundException("User not found"));
  }

  private Cart findCart(User user) {
    return cartRepository.findByUser(user).orElse(null);
  }

  private Cart findOrCreateCart(User user) {
    return cartRepository.findByUser(user)
        .orElseGet(() -> {
          Cart newCart = Cart.builder()
              .user(user)
              .build();
          return cartRepository.save(newCart);
        });
  }

  private CartItem findCartItemById(Long cartItemId) {
    return cartItemRepository.findById(cartItemId)
        .orElseThrow(() -> new WebBadRequestException(ErrorMessage.ITEM_NOT_FOUND));
  }

  private void validateCartItemOwnership(CartItem cartItem, String username) {
    User user = findUserByUsername(username);
    Cart cart = findOrCreateCart(user);

    if (!cartItem.getCart().getId().equals(cart.getId())) {
      throw new WebBadRequestException(ErrorMessage.ITEM_NOT_FOUND);
    }
  }
}
