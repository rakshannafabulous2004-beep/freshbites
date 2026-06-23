package com.freshbite.dto;

public class CartItemResponse {

    private Long id;
    private Long mealId;
    private String mealName;
    private String imageUrl;
    private Double price;
    private Integer quantity;
    private Double subtotal;

    public CartItemResponse() {
    }

    public CartItemResponse(Long id, Long mealId, String mealName, String imageUrl,
                            Double price, Integer quantity, Double subtotal) {
        this.id = id;
        this.mealId = mealId;
        this.mealName = mealName;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantity = quantity;
        this.subtotal = subtotal;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
}
