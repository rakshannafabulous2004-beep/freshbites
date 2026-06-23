package com.freshbite.dto;

public class MealResponse {

    private Long id;
    private String name;
    private String description;
    private String category;
    private Integer calories;
    private Double protein;
    private Double carbs;
    private Double fats;
    private Double price;
    private String imageUrl;

    public MealResponse() {
    }

    public MealResponse(Long id, String name, String description, String category,
                        Integer calories, Double protein, Double carbs, Double fats,
                        Double price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fats = fats;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public Double getCarbs() {
        return carbs;
    }

    public void setCarbs(Double carbs) {
        this.carbs = carbs;
    }

    public Double getFats() {
        return fats;
    }

    public void setFats(Double fats) {
        this.fats = fats;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
