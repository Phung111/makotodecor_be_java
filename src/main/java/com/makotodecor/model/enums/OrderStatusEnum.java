package com.makotodecor.model.enums;

import lombok.Getter;

@Getter
public enum OrderStatusEnum implements PersistableEnum<String> {

  NEW("NEW"),
  PENDING_DEPOSIT("PENDING_DEPOSIT"),
  DEPOSITED("DEPOSITED"),
  PAID("PAID"),
  PROCESSING("PROCESSING"),
  CANCELLED("CANCELLED"),
  COMPLETED("COMPLETED");

  private final String value;

  OrderStatusEnum(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }
}
