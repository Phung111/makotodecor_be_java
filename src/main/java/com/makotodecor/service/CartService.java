package com.makotodecor.service;

import com.makotodecor.model.AddCartItemRequest;
import com.makotodecor.model.CartItemIdRequest;
import com.makotodecor.model.CartCountResponse;
import com.makotodecor.model.CartResponse;
import com.makotodecor.model.ChangeQuantityCartItemRequest;

public interface CartService {

  CartResponse getCart(String username);

  CartCountResponse getCartCount(String username);

  CartResponse addCartItem(String username, AddCartItemRequest request);

  CartResponse increaseCartItem(String username, CartItemIdRequest request);

  CartResponse decreaseCartItem(String username, CartItemIdRequest request);

  CartResponse changeQuantityCartItem(String username, ChangeQuantityCartItemRequest request);

  CartResponse deleteCartItem(String username, CartItemIdRequest request);
}
