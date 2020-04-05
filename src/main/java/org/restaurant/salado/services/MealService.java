package org.restaurant.salado.services;

import org.restaurant.salado.entities.Meal;

import java.util.List;

/**
 * @author Haytam DAHRI
 */
public interface MealService {

    Meal saveMeal(Meal meal);

    boolean deleteMeal(Long id);

    Meal getMeal(Long id);

    List<Meal> getMeals();

    List<Meal> getMealsAndIncrementViews();

    List<Meal> getPopularMeals();

}
