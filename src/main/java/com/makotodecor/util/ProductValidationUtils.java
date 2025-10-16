package com.makotodecor.util;

import com.makotodecor.exceptions.ProductValidationException;
import com.makotodecor.exceptions.base.ErrorMessage;
import com.makotodecor.model.CreateProductRequest;
import com.makotodecor.model.ImageInfo;
import com.makotodecor.model.ProductColorRequest;
import com.makotodecor.model.ProductPriceRequest;
import com.makotodecor.model.UpdateProductRequest;

import java.util.Arrays;
import java.util.List;

public class ProductValidationUtils {

  private static final int MIN_NAME_LENGTH = 3;
  private static final int MAX_NAME_LENGTH = 255;
  private static final long MIN_DISCOUNT = 0L;
  private static final long MAX_DISCOUNT = 100L;
  private static final long MIN_BASE_SOLD = 0L;

  private ProductValidationUtils() {
  }

  public static void validateCreateProductRequest(CreateProductRequest request) {
    ProductValidationException exception = new ProductValidationException();

    validateName(request.getName(), "name", exception);

    if (request.getCategoryId() == null) {
      exception.addError("categoryId", ErrorMessage.PRODUCT_CATEGORY_REQUIRED);
    }

    validateStatus(request.getStatus(), "status", exception);

    validatePrices(request.getPrices(), "prices", exception);

    if (request.getDiscount() != null) {
      validateDiscount(request.getDiscount(), "discount", exception);
    }

    if (request.getBaseSold() != null) {
      validateBaseSold(request.getBaseSold(), "baseSold", exception);
    }

    if (request.getDefaultImage() == null) {
      exception.addError("defaultImage", ErrorMessage.PRODUCT_DEFAULT_IMAGE_REQUIRED);
    } else {
      validateImage(request.getDefaultImage(), "defaultImage", exception);
    }

    if (request.getOtherImages() != null) {
      validateImageList(request.getOtherImages(), "otherImages", exception);
    }

    if (request.getDetailImages() != null) {
      validateImageList(request.getDetailImages(), "detailImages", exception);
    }

    if (request.getColors() != null) {
      validateColorList(request.getColors(), "colors", exception);
    }

    if (exception.hasErrors()) {
      throw exception;
    }
  }

  public static void validateUpdateProductRequest(UpdateProductRequest request) {
    ProductValidationException exception = new ProductValidationException();

    if (request.getName() != null) {
      validateName(request.getName(), "name", exception);
    }

    if (request.getCategoryId() == null) {
      exception.addError("categoryId", ErrorMessage.PRODUCT_CATEGORY_REQUIRED);
    }

    if (request.getStatus() != null) {
      validateStatus(request.getStatus(), "status", exception);
    }

    if (request.getPrices() != null) {
      validatePrices(request.getPrices(), "prices", exception);
    }

    if (request.getDiscount() != null) {
      validateDiscount(request.getDiscount(), "discount", exception);
    }

    if (request.getBaseSold() != null) {
      validateBaseSold(request.getBaseSold(), "baseSold", exception);
    }

    if (request.getDefaultImage() != null) {
      validateImage(request.getDefaultImage(), "defaultImage", exception);
    }

    if (request.getOtherImages() != null) {
      validateImageList(request.getOtherImages(), "otherImages", exception);
    }

    if (request.getDetailImages() != null) {
      validateImageList(request.getDetailImages(), "detailImages", exception);
    }

    if (request.getColors() != null) {
      validateColorList(request.getColors(), "colors", exception);
    }

    if (exception.hasErrors()) {
      throw exception;
    }
  }

  private static void validateName(String name, String target, ProductValidationException exception) {
    if (name == null || name.trim().isEmpty()) {
      exception.addError(target, ErrorMessage.PRODUCT_NAME_REQUIRED);
      return;
    }

    int length = name.trim().length();
    if (length < MIN_NAME_LENGTH || length > MAX_NAME_LENGTH) {
      exception.addError(target, ErrorMessage.PRODUCT_NAME_INVALID);
    }
  }

  private static void validateStatus(Object status, String target, ProductValidationException exception) {
    if (status == null) {
      exception.addError(target, ErrorMessage.PRODUCT_STATUS_REQUIRED);
      return;
    }

    String statusValue = status.toString();
    boolean isValid = Arrays.stream(com.makotodecor.model.ProductStatusEnum.values())
        .anyMatch(e -> e.getValue().equals(statusValue));

    if (!isValid) {
      exception.addError(target, ErrorMessage.PRODUCT_STATUS_INVALID);
    }
  }

  private static void validatePrices(List<ProductPriceRequest> prices, String target,
      ProductValidationException exception) {
    if (prices == null) {
      exception.addError(target, ErrorMessage.PRODUCT_PRICES_REQUIRED);
      return;
    }

    if (prices.isEmpty()) {
      exception.addError(target, ErrorMessage.PRODUCT_PRICES_EMPTY);
      return;
    }

    for (int i = 0; i < prices.size(); i++) {
      ProductPriceRequest price = prices.get(i);
      String priceTarget = target + "[" + i + "].price";

      if (price.getPrice() == null || price.getPrice() <= 0) {
        exception.addError(priceTarget, ErrorMessage.PRODUCT_PRICE_INVALID);
      }
    }
  }

  private static void validateDiscount(Long discount, String target, ProductValidationException exception) {
    if (discount < MIN_DISCOUNT || discount > MAX_DISCOUNT) {
      exception.addError(target, ErrorMessage.PRODUCT_DISCOUNT_INVALID);
    }
  }

  private static void validateBaseSold(Long baseSold, String target, ProductValidationException exception) {
    if (baseSold < MIN_BASE_SOLD) {
      exception.addError(target, ErrorMessage.PRODUCT_BASE_SOLD_INVALID);
    }
  }

  private static void validateImage(ImageInfo image, String target, ProductValidationException exception) {
    if (image.getUrl() == null || image.getUrl().trim().isEmpty()) {
      exception.addError(target + ".url", ErrorMessage.PRODUCT_IMAGE_URL_REQUIRED);
    }

    if (image.getImgTypeId() == null) {
      exception.addError(target + ".imgTypeId", ErrorMessage.PRODUCT_IMAGE_TYPE_REQUIRED);
    }
  }

  private static void validateImageList(List<ImageInfo> images, String target,
      ProductValidationException exception) {
    for (int i = 0; i < images.size(); i++) {
      ImageInfo image = images.get(i);
      String imageTarget = target + "[" + i + "]";

      if (image.getUrl() == null || image.getUrl().trim().isEmpty()) {
        exception.addError(imageTarget + ".url", ErrorMessage.PRODUCT_IMAGE_URL_REQUIRED);
      }

      if (image.getImgTypeId() == null) {
        exception.addError(imageTarget + ".imgTypeId", ErrorMessage.PRODUCT_IMAGE_TYPE_REQUIRED);
      }
    }
  }

  private static void validateColorList(List<ProductColorRequest> colors, String target,
      ProductValidationException exception) {
    for (int i = 0; i < colors.size(); i++) {
      ProductColorRequest color = colors.get(i);

      if (color.getImage() != null) {
        String colorImageTarget = target + "[" + i + "].image";

        if (color.getImage().getUrl() == null || color.getImage().getUrl().trim().isEmpty()) {
          exception.addError(colorImageTarget + ".url", ErrorMessage.PRODUCT_IMAGE_URL_REQUIRED);
        }

        if (color.getImage().getImgTypeId() == null) {
          exception.addError(colorImageTarget + ".imgTypeId", ErrorMessage.PRODUCT_IMAGE_TYPE_REQUIRED);
        }
      }
    }
  }
}
