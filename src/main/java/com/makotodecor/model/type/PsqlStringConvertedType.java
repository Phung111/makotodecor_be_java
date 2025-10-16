package com.makotodecor.model.type;

import com.makotodecor.model.enums.PsqlEnumT;
import com.makotodecor.model.enums.PersistableEnum;

public abstract class PsqlStringConvertedType<E extends Enum<E> & PersistableEnum<String>>
    extends PsqlEnumT<String, E> {

  @Override
  protected Class<String> getPersistentType() {
    return String.class;
  }
}
