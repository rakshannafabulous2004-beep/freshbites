package com.freshbite.service;

import com.freshbite.dto.OrderRequest;
import com.freshbite.dto.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse placeOrder(Long userId, OrderRequest request);

    List<OrderResponse> getUserOrders(Long userId);

    List<OrderResponse> getAllOrders();

    OrderResponse updateOrderStatus(Long orderId, String status);
}
