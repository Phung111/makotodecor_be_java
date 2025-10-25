package com.makotodecor.model.enums;

import lombok.Getter;

@Getter
public enum ImgTypeStatusEnum implements PersistableEnum<String> {

  ACTIVE("ACTIVE"),
  INACTIVE("INACTIVE");

  private final String value;

  ImgTypeStatusEnum(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }
}
