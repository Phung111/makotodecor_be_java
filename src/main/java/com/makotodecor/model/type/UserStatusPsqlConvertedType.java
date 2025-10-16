package com.makotodecor.model.type;

import com.makotodecor.model.enums.UserStatusEnum;

public class UserStatusPsqlConvertedType extends PsqlStringConvertedType<UserStatusEnum> {

  @Override
  protected Class<UserStatusEnum> getEnumClass() {
    return UserStatusEnum.class;
  }

  @Override
  public Class<UserStatusEnum> returnedClass() {
    return UserStatusEnum.class;
  }
}
