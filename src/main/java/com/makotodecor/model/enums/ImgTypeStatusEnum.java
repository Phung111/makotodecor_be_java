package com.makotodecor.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImgTypeStatusEnum implements PersistableEnum<String> {

  ACTIVE("ACTIVE"),
  INACTIVE("INACTIVE");

  private final String value;
}
