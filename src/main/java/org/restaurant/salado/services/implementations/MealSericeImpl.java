package org.restaurant.salado.services.implementations;

import org.restaurant.salado.entities.Meal;
import org.restaurant.salado.providers.ValuesProvider;
import org.restaurant.salado.repositories.MealRepository;
import org.restaurant.salado.services.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Haytam DAHRI
 */
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
    public Meal getMeal(Long id) {
        return this.mealRepository.findById(id).orElse(null);
    }

    @Override
    public List<Meal> getMeals() {
        return this.mealRepository.findAll();
    }

    @Override
    public Page<Meal> getMeals(int page, int size) {
        return this.mealRepository.findAll(PageRequest.of(page, size, Sort.Direction.DESC, "id"));
    }

    /**
     * Increment views and return meals list
     * @return List<Meal>
     */
    @Override
    public List<Meal> getPopularMeals(int page, int size) {
        return this.mealRepository.findTop10ByOrderByViewsDesc(PageRequest.of(page, size)).getContent();
    }
}
