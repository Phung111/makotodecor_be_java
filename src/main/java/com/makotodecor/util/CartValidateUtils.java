package com.makotodecor.util;

import com.makotodecor.exceptions.CartValidationException;
import com.makotodecor.exceptions.base.ErrorMessage;
import com.makotodecor.model.AddCartItemRequest;
import com.makotodecor.model.ChangeQuantityCartItemRequest;

public class CartValidateUtils {

  private static final long MIN_QUANTITY = 1L;
  private static final long MAX_QUANTITY = 999L;

  private CartValidateUtils() {
  }

  public static void validateAddCartItemRequest(AddCartItemRequest request) {
    CartValidationException exception = new CartValidationException();

    if (request.getProductId() == null) {
      exception.addError("productId", ErrorMessage.CART_PRODUCT_ID_REQUIRED);
    }

    if (request.getSizeId() == null) {
      exception.addError("sizeId", ErrorMessage.CART_SIZE_ID_REQUIRED);
    }

    if (request.getQuantity() == null) {
      exception.addError("quantity", ErrorMessage.CART_QUANTITY_REQUIRED);
    } else {
      validateQuantity(request.getQuantity(), "quantity", exception);
    }

    if (exception.hasErrors()) {
      throw exception;
    }
  }

  public static void validateChangeQuantityCartItemRequest(ChangeQuantityCartItemRequest request) {
    CartValidationException exception = new CartValidationException();

    if (request.getCartItemId() == null) {
      exception.addError("cartItemId", ErrorMessage.CART_ITEM_ID_REQUIRED);
    }

    if (request.getQuantity() == null) {
      exception.addError("quantity", ErrorMessage.CART_QUANTITY_REQUIRED);
    } else {
      validateQuantity(request.getQuantity(), "quantity", exception);
    }

    if (exception.hasErrors()) {
      throw exception;
    }
  }

  private static void validateQuantity(Long quantity, String target, CartValidationException exception) {
    if (quantity < MIN_QUANTITY || quantity > MAX_QUANTITY) {
      exception.addError(target, ErrorMessage.CART_QUANTITY_INVALID);
    }
  }
}
