package com.makotodecor.model.type;

import com.makotodecor.model.enums.ImgTypeStatusEnum;

public class ImgTypeStatusPsqlConvertedType extends PsqlStringConvertedType<ImgTypeStatusEnum> {

  @Override
  protected Class<ImgTypeStatusEnum> getEnumClass() {
    return ImgTypeStatusEnum.class;
  }

  @Override
  public Class<ImgTypeStatusEnum> returnedClass() {
    return ImgTypeStatusEnum.class;
  }
}
