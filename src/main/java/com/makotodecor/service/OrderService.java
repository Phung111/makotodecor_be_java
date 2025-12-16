package com.makotodecor.service;

import com.makotodecor.model.CreateOrderRequest;
import com.makotodecor.model.OrderDetailResponse;
import com.makotodecor.model.OrdersPagedResponse;
import com.makotodecor.model.UpdateOrderStatusRequest;
import com.makotodecor.model.dto.OrderPagedCriteria;

public interface OrderService {

  OrdersPagedResponse getOrdersPaged(OrderPagedCriteria criteria);

  OrderDetailResponse getOrder(Long orderId);

  void updateOrderStatus(UpdateOrderStatusRequest request);

  OrderDetailResponse placeOrder(CreateOrderRequest request, String username);
}
