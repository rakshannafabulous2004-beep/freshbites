package com.freshbite.controller;

import com.freshbite.dto.ApiResponse;
import com.freshbite.dto.OrderRequest;
import com.freshbite.dto.OrderResponse;
import com.freshbite.dto.OrderStatusRequest;
import com.freshbite.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse> placeOrder(@PathVariable Long userId,
                                                  @Valid @RequestBody OrderRequest request) {
        OrderResponse order = orderService.placeOrder(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order placed successfully", order));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
        List<OrderResponse> orders = orderService.getUserOrders(userId);
        return ResponseEntity.ok(ApiResponse.success("Orders retrieved", orders));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(ApiResponse.success("All orders retrieved", orders));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse> updateOrderStatus(@PathVariable Long orderId,
                                                         @Valid @RequestBody OrderStatusRequest request) {
        OrderResponse order = orderService.updateOrderStatus(orderId, request.getOrderStatus());
        return ResponseEntity.ok(ApiResponse.success("Order status updated", order));
    }
}
