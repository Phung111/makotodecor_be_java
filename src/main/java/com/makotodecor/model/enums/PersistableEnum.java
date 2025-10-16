package com.makotodecor.model.enums;

import java.util.Optional;

public interface PersistableEnum<T> {

  T getValue();

  static <T, E extends Enum<E> & PersistableEnum<T>> Optional<E> fromValue(Class<E> enumClass, T value) {
    for (E enumValue : enumClass.getEnumConstants()) {
      if (enumValue.getValue().equals(value)) {
        return Optional.of(enumValue);
      }
    }
    return Optional.empty();
  }
}
