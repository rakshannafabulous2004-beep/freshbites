package com.freshbite.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private Long id;
    private Long userId;
    private String customerName;
    private String phone;
    private String deliveryAddress;
    private Double totalAmount;
    private String orderStatus;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;

    public OrderResponse() {
    }

    public OrderResponse(Long id, Long userId, String customerName, String phone,
                         String deliveryAddress, Double totalAmount, String orderStatus,
                         LocalDateTime createdAt, List<OrderItemResponse> items) {
        this.id = id;
        this.userId = userId;
        this.customerName = customerName;
        this.phone = phone;
        this.deliveryAddress = deliveryAddress;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResponse> items) {
        this.items = items;
    }
}
