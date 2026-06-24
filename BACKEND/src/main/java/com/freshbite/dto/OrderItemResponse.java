package com.freshbite.dto;

import java.util.List;

public class OrderItemResponse {

    private Long id;
    private Long mealId;
    private String mealName;
    private Integer quantity;
    private Double price;

    public OrderItemResponse() {
    }

    public OrderItemResponse(Long id, Long mealId, String mealName, Integer quantity, Double price) {
        this.id = id;
        this.mealId = mealId;
        this.mealName = mealName;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMealId() {
        return mealId;
    }

    public void setMealId(Long mealId) {
        this.mealId = mealId;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
