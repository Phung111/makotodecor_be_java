package com.makotodecor.model.type;

import com.makotodecor.model.enums.CategoryStatusEnum;

public class CategoryStatusPsqlConvertedType extends PsqlStringConvertedType<CategoryStatusEnum> {

  @Override
  protected Class<CategoryStatusEnum> getEnumClass() {
    return CategoryStatusEnum.class;
  }

  @Override
  public Class<CategoryStatusEnum> returnedClass() {
    return CategoryStatusEnum.class;
  }
}
