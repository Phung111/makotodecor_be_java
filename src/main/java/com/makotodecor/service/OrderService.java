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

  OrdersPagedResponse getMyOrdersPaged(OrderPagedCriteria criteria, String username);

  OrderDetailResponse getMyOrder(Long orderId, String username);

  OrderDetailResponse updatePaymentProof(Long orderId, com.makotodecor.model.ImageInfo paymentProof, String username);
}
