package org.restaurant.salado.services;

import org.restaurant.salado.entities.Meal;

import java.util.Collection;

public interface MealService {

    public Meal saveMeal(Meal meal);

    public boolean deleteMeal(Long id);

    public Collection<Meal> getMeals();

}
