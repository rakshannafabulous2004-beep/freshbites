package com.freshbite.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class MealRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Calories is required")
    @Positive(message = "Calories must be positive")
    private Integer calories;

    @NotNull(message = "Protein is required")
    @Positive(message = "Protein must be positive")
    private Double protein;

    @NotNull(message = "Carbs is required")
    @Positive(message = "Carbs must be positive")
    private Double carbs;

    @NotNull(message = "Fats is required")
    @Positive(message = "Fats must be positive")
    private Double fats;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    private String imageUrl;

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
