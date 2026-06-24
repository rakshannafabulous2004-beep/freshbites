package com.freshbite.service.impl;

import com.freshbite.dto.MealRequest;
import com.freshbite.dto.MealResponse;
import com.freshbite.entity.Meal;
import com.freshbite.exception.ResourceNotFoundException;
import com.freshbite.repository.MealRepository;
import com.freshbite.service.MealService;
import com.freshbite.util.MapperUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;

    public MealServiceImpl(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MealResponse> getAllMeals() {
        return MapperUtil.toMealResponseList(mealRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MealResponse> getMealsByCategory(String category) {
        return MapperUtil.toMealResponseList(mealRepository.findByCategory(category));
    }

    @Override
    @Transactional(readOnly = true)
    public MealResponse getMealById(Long id) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found with id: " + id));
        return MapperUtil.toMealResponse(meal);
    }

    @Override
    public MealResponse createMeal(MealRequest request) {
        Meal meal = new Meal();
        MapperUtil.applyMealRequest(meal, request);
        Meal savedMeal = mealRepository.save(meal);
        return MapperUtil.toMealResponse(savedMeal);
    }

    @Override
    public MealResponse updateMeal(Long id, MealRequest request) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found with id: " + id));
        MapperUtil.applyMealRequest(meal, request);
        Meal updatedMeal = mealRepository.save(meal);
        return MapperUtil.toMealResponse(updatedMeal);
    }

    @Override
    public void deleteMeal(Long id) {
        if (!mealRepository.existsById(id)) {
            throw new ResourceNotFoundException("Meal not found with id: " + id);
        }
        mealRepository.deleteById(id);
    }
}
