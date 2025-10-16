package com.makotodecor.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatusEnum implements PersistableEnum<String> {

  NEW("NEW"),
  DEPOSITED("DEPOSITED"),
  CANCELLED("CANCELLED"),
  COMPLETED("COMPLETED");

  private final String value;
}
