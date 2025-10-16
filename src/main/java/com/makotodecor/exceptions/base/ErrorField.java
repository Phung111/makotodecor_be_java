package com.makotodecor.exceptions.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorField {
  private String code;
  private String message;
  private String target;
}
