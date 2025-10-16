package com.makotodecor.model.type;

import com.makotodecor.model.enums.ProductStatusEnum;

public class ProductStatusPsqlConvertedType extends PsqlStringConvertedType<ProductStatusEnum> {

  @Override
  protected Class<ProductStatusEnum> getEnumClass() {
    return ProductStatusEnum.class;
  }

  @Override
  public Class<ProductStatusEnum> returnedClass() {
    return ProductStatusEnum.class;
  }
}
