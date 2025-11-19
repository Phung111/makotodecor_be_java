package com.makotodecor.model.enums;

import lombok.Getter;

@Getter
public enum CategoryStatusEnum implements PersistableEnum<String> {

  ACTIVE("ACTIVE"),
  INACTIVE("INACTIVE");

  private final String value;

  CategoryStatusEnum(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }
}
