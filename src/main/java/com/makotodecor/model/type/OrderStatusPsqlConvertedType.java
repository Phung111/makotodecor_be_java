package com.makotodecor.model.type;

import com.makotodecor.model.enums.OrderStatusEnum;

public class OrderStatusPsqlConvertedType extends PsqlStringConvertedType<OrderStatusEnum> {

  @Override
  protected Class<OrderStatusEnum> getEnumClass() {
    return OrderStatusEnum.class;
  }

  @Override
  public Class<OrderStatusEnum> returnedClass() {
    return OrderStatusEnum.class;
  }
}
