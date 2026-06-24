package com.freshbite.controller;

import com.freshbite.dto.ApiResponse;
import com.freshbite.dto.MealRequest;
import com.freshbite.dto.MealResponse;
import com.freshbite.service.MealService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meals")
public class MealController {

    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllMeals(@RequestParam(required = false) String category) {
        List<MealResponse> meals;
        if (category != null && !category.isBlank()) {
            meals = mealService.getMealsByCategory(category);
        } else {
            meals = mealService.getAllMeals();
        }
        return ResponseEntity.ok(ApiResponse.success("Meals retrieved", meals));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getMealById(@PathVariable Long id) {
        MealResponse meal = mealService.getMealById(id);
        return ResponseEntity.ok(ApiResponse.success("Meal retrieved", meal));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createMeal(@Valid @RequestBody MealRequest request) {
        MealResponse meal = mealService.createMeal(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Meal created", meal));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateMeal(@PathVariable Long id,
                                                  @Valid @RequestBody MealRequest request) {
        MealResponse meal = mealService.updateMeal(id, request);
        return ResponseEntity.ok(ApiResponse.success("Meal updated", meal));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteMeal(@PathVariable Long id) {
        mealService.deleteMeal(id);
        return ResponseEntity.ok(ApiResponse.success("Meal deleted"));
    }
}
