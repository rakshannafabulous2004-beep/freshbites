package com.freshbite.config;

import com.freshbite.entity.Meal;
import com.freshbite.repository.MealRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final MealRepository mealRepository;

    public DataInitializer(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    @Override
    public void run(String... args) {
        if (mealRepository.count() > 0) {
            return;
        }

        mealRepository.save(createMeal(
                "Grilled Chicken Bowl",
                "Lean grilled chicken with quinoa, steamed broccoli, and cherry tomatoes.",
                "High Protein",
                420, 38.0, 35.0, 12.0, 12.99,
                "images/meal-chicken.svg"
        ));

        mealRepository.save(createMeal(
                "Mediterranean Salad",
                "Fresh mixed greens with feta, olives, cucumber, and lemon dressing.",
                "Low Calorie",
                280, 12.0, 18.0, 18.0, 9.49,
                "images/meal-salad.svg"
        ));

        mealRepository.save(createMeal(
                "Salmon & Asparagus",
                "Oven-baked salmon fillet served with roasted asparagus and brown rice.",
                "Keto",
                390, 32.0, 22.0, 18.0, 14.99,
                "images/meal-salmon.svg"
        ));

        mealRepository.save(createMeal(
                "Veggie Power Wrap",
                "Whole wheat wrap filled with hummus, avocado, spinach, and roasted veggies.",
                "Vegetarian",
                350, 14.0, 42.0, 14.0, 10.99,
                "images/meal-wrap.svg"
        ));

        mealRepository.save(createMeal(
                "Protein Oatmeal Bowl",
                "Steel-cut oats with almond butter, berries, and a scoop of protein powder.",
                "Breakfast",
                310, 22.0, 38.0, 8.0, 8.99,
                "images/meal-oatmeal.svg"
        ));

        mealRepository.save(createMeal(
                "Turkey Lettuce Wraps",
                "Seasoned ground turkey with bell peppers served in crisp lettuce cups.",
                "Low Carb",
                260, 28.0, 12.0, 10.0, 11.49,
                "images/meal-turkey.svg"
        ));
    }

    private Meal createMeal(String name, String description, String category,
                            int calories, double protein, double carbs, double fats,
                            double price, String imageUrl) {
        Meal meal = new Meal();
        meal.setName(name);
        meal.setDescription(description);
        meal.setCategory(category);
        meal.setCalories(calories);
        meal.setProtein(protein);
        meal.setCarbs(carbs);
        meal.setFats(fats);
        meal.setPrice(price);
        meal.setImageUrl(imageUrl);
        return meal;
    }
}
