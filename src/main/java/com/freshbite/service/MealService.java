package com.freshbite.service;

import com.freshbite.dto.MealRequest;
import com.freshbite.dto.MealResponse;

import java.util.List;

public interface MealService {

    List<MealResponse> getAllMeals();

    List<MealResponse> getMealsByCategory(String category);

    MealResponse getMealById(Long id);

    MealResponse createMeal(MealRequest request);

    MealResponse updateMeal(Long id, MealRequest request);

    void deleteMeal(Long id);
}
