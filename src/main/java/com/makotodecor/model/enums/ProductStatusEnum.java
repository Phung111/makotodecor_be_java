package com.makotodecor.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatusEnum implements PersistableEnum<String> {

  ACTIVE("ACTIVE"),
  INACTIVE("INACTIVE"),
  OUT_OF_STOCK("OUT_OF_STOCK"),
  DISCONTINUED("DISCONTINUED");

  private final String value;
}
