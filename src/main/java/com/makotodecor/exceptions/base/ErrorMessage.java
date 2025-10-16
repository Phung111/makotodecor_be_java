package com.makotodecor.exceptions.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorMessage {

  PRODUCT_NOT_FOUND("P001", "product.not.found"),
  PRODUCT_INVALID("P002", "product.invalid"),

  // Product validation errors
  PRODUCT_NAME_REQUIRED("P003", "product.name.required"),
  PRODUCT_NAME_INVALID("P004", "product.name.invalid"),
  PRODUCT_CATEGORY_REQUIRED("P005", "product.category.required"),
  PRODUCT_STATUS_REQUIRED("P006", "product.status.required"),
  PRODUCT_STATUS_INVALID("P007", "product.status.invalid"),
  PRODUCT_PRICES_REQUIRED("P008", "product.prices.required"),
  PRODUCT_PRICES_EMPTY("P009", "product.prices.empty"),
  PRODUCT_PRICE_INVALID("P010", "product.price.invalid"),
  PRODUCT_DISCOUNT_INVALID("P011", "product.discount.invalid"),
  PRODUCT_BASE_SOLD_INVALID("P012", "product.base.sold.invalid"),
  PRODUCT_DEFAULT_IMAGE_REQUIRED("P013", "product.default.image.required"),
  PRODUCT_IMAGE_URL_REQUIRED("P014", "product.image.url.required"),
  PRODUCT_IMAGE_TYPE_REQUIRED("P015", "product.image.type.required"),

  // Category errors
  CATEGORY_NOT_FOUND("C001", "category.not.found"),

  // General errors
  ITEM_NOT_FOUND("G001", "item.not.found"),

  // Authentication validation errors
  AUTH_USERNAME_REQUIRED("A001", "auth.username.required"),
  AUTH_USERNAME_INVALID("A002", "auth.username.invalid"),
  AUTH_PASSWORD_REQUIRED("A003", "auth.password.required"),
  AUTH_PASSWORD_TOO_SHORT("A004", "auth.password.too.short"),
  AUTH_PASSWORD_TOO_WEAK("A005", "auth.password.too.weak"),
  AUTH_EMAIL_REQUIRED("A006", "auth.email.required"),
  AUTH_EMAIL_INVALID("A007", "auth.email.invalid"),
  AUTH_NAME_REQUIRED("A008", "auth.name.required"),
  AUTH_PHONE_INVALID("A009", "auth.phone.invalid"),
  AUTH_REFRESH_TOKEN_REQUIRED("A010", "auth.refresh.token.required"),
  AUTH_REFRESH_TOKEN_INVALID("A011", "auth.refresh.token.invalid"),
  AUTH_CURRENT_PASSWORD_REQUIRED("A012", "auth.current.password.required"),
  AUTH_NEW_PASSWORD_REQUIRED("A013", "auth.new.password.required"),
  AUTH_CONFIRM_PASSWORD_REQUIRED("A014", "auth.confirm.password.required"),
  AUTH_PASSWORDS_NOT_MATCH("A015", "auth.passwords.not.match"),
  AUTH_PASSWORD_SAME_AS_CURRENT("A016", "auth.password.same.as.current"),

  // Authentication business logic errors
  AUTH_USERNAME_ALREADY_EXISTS("A017", "auth.username.already.exists"),
  AUTH_EMAIL_ALREADY_EXISTS("A018", "auth.email.already.exists"),
  AUTH_USER_NOT_FOUND("A019", "auth.user.not.found"),
  AUTH_INVALID_CREDENTIALS("A020", "auth.invalid.credentials"),
  AUTH_CURRENT_PASSWORD_INCORRECT("A021", "auth.current.password.incorrect"),

  // File upload errors
  FILE_EMPTY("F001", "file.empty"),
  FILE_INVALID_TYPE("F002", "file.invalid.type"),
  FILE_TOO_LARGE("F003", "file.too.large"),
  FILE_UPLOAD_FAILED("F004", "file.upload.failed"),
  FILE_NO_FILES_PROVIDED("F005", "file.no.files.provided"),

  UTILS_SORTABLE_COLUMNS_DOES_NOT_CONTAIN_VALUE("UT001", "utils.sortable.columns.does.not.contain.value")

  ;

  private final String code;
  private final String message;
}
