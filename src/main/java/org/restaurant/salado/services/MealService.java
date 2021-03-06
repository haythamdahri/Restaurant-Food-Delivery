package org.restaurant.salado.services;

import org.restaurant.salado.entities.Meal;
import org.restaurant.salado.models.MealRequest;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

/**
 * @author Haytam DAHRI
 */
public interface MealService {

    Meal saveMeal(Meal meal);

    Meal saveMeal(MealRequest mealRequest) throws IOException;

    boolean deleteMeal(Long id);

    boolean undoMealDelete(Long id);

    Meal getMeal(Long id);

    Meal getExistingMeal(Long id);

    List<Meal> getMeals();

    List<Meal> getAllMeals();

    Page<Meal> getMeals(int page, int size);

    Page<Meal> getMeals(String search, int page, int size);

    Page<Meal> getAllMeals(String search, int page, int size);

    List<Meal> getPopularMeals(int page, int size);

    List<Meal> getUserPreferredMeals(String email);

}
