package org.restaurant.salado.services.impl;

import org.restaurant.salado.entities.Meal;
import org.restaurant.salado.repositories.MealRepository;
import org.restaurant.salado.services.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Haytam DAHRI
 */
@Service
@Transactional
public class MealServiceImpl implements MealService {

    private MealRepository mealRepository;

    @Autowired
    public void setMealRepository(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

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
     * Get popular meals list
     *
     * @return List<Meal>
     */
    @Override
    public List<Meal> getPopularMeals(int page, int size) {
        return this.mealRepository.findTop10ByOrderByViewsDesc(PageRequest.of(page, size)).getContent();
    }

    @Override
    public List<Meal> getUserPreferredMeals(String email) {
        return this.mealRepository.findMealByUsersPreferences_UserIdEmail(email);
    }
}
