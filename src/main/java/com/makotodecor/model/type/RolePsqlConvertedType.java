package com.makotodecor.model.type;

import com.makotodecor.model.enums.RoleEnum;

public class RolePsqlConvertedType extends PsqlStringConvertedType<RoleEnum> {

  @Override
  protected Class<RoleEnum> getEnumClass() {
    return RoleEnum.class;
  }

  @Override
  public Class<RoleEnum> returnedClass() {
    return RoleEnum.class;
  }
}
