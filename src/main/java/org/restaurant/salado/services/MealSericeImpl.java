package org.restaurant.salado.services;

import org.restaurant.salado.entities.Meal;
import org.restaurant.salado.repositories.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MealSericeImpl implements MealService {

    @Autowired
    private MealRepository mealRepository;

    @Override
    public Meal saveMeal(Meal meal) {
        return this.mealRepository.save(meal);
    }

    @Override
    public boolean deleteMeal(Long id) {
        this.mealRepository.deleteById(id);
        return true;
    }

    @Override
    public Collection<Meal> getMeals() {
        return this.mealRepository.findAll();
    }
}
