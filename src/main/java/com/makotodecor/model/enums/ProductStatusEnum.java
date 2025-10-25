package com.makotodecor.model.enums;

import lombok.Getter;

@Getter
public enum ProductStatusEnum implements PersistableEnum<String> {

  ACTIVE("ACTIVE"),
  INACTIVE("INACTIVE"),
  OUT_OF_STOCK("OUT_OF_STOCK"),
  DISCONTINUED("DISCONTINUED");

  private final String value;

  ProductStatusEnum(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }
}
