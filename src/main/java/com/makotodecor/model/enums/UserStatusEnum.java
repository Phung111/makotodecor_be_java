package com.makotodecor.model.enums;

import lombok.Getter;

@Getter
public enum UserStatusEnum implements PersistableEnum<String> {

  ACTIVE("ACTIVE"),
  INACTIVE("INACTIVE");

  private final String value;

  UserStatusEnum(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }
}
