package org.restaurant.salado.services.impl;

import org.apache.commons.io.FilenameUtils;
import org.restaurant.salado.entities.Meal;
import org.restaurant.salado.entities.RestaurantFile;
import org.restaurant.salado.exceptions.BusinessException;
import org.restaurant.salado.models.MealRequest;
import org.restaurant.salado.repositories.MealRepository;
import org.restaurant.salado.services.MealService;
import org.restaurant.salado.services.RestaurantFileService;
import org.restaurant.salado.utils.RestaurantUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

/**
 * @author Haytam DAHRI
 */
@Service
@Transactional
public class MealServiceImpl implements MealService {

    private MealRepository mealRepository;
    private RestaurantFileService restaurantFileService;

    @Autowired
    public void setMealRepository(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    @Autowired
    public void setRestaurantFileService(RestaurantFileService restaurantFileService) {
        this.restaurantFileService = restaurantFileService;
    }

    @Override
    public Meal saveMeal(Meal meal) {
        return this.mealRepository.save(meal);
    }

    @Override
    public Meal saveMeal(MealRequest mealRequest) throws IOException {
        // Get Or Create the meal
        Meal meal = mealRequest.getId() != null ? this.mealRepository.findById(mealRequest.getId()).orElse(new Meal()) : new Meal();
        // Set Meal values
        meal.setName(mealRequest.getName());
        meal.setPrice(mealRequest.getPrice());
        meal.setSalePrice(mealRequest.getSalePrice());
        meal.setStock(mealRequest.getStock());
        meal.setViews(mealRequest.getViews());
        // Create Meal File if update image is true
        if (mealRequest.isUpdateImage() || meal.getId() == null) {
            RestaurantFile restaurantFile = new RestaurantFile(null, FilenameUtils.removeExtension(mealRequest.getName()), RestaurantUtils.getExtensionByApacheCommonLib(mealRequest.getName()), MediaType.IMAGE_PNG.toString(), mealRequest.getImage().getBytes(), null);
            restaurantFile = this.restaurantFileService.saveRestaurantFile(restaurantFile);
            meal.setImage(restaurantFile);
        }
        return this.mealRepository.save(meal);
    }

    @Override
    public boolean deleteMeal(Long id) {
        Meal meal = this.mealRepository.findById(id).orElseThrow(BusinessException::new);
        meal.setDeleted(true);
        this.mealRepository.save(meal);
        return true;
    }

    @Override
    public boolean undoMealDelete(Long id) {
        Meal meal = this.mealRepository.findById(id).orElseThrow(BusinessException::new);
        meal.setDeleted(false);
        this.mealRepository.save(meal);
        return true;
    }

    @Override
    public Meal getMeal(Long id) {
        return this.mealRepository.findById(id).orElse(null);
    }

    @Override
    public Meal getExistingMeal(Long id) {
        return this.mealRepository.findByIdAndDeletedFalse(id).orElse(null);
    }

    @Override
    public List<Meal> getMeals() {
        return this.mealRepository.findAllByDeletedFalse();
    }

    @Override
    public List<Meal> getAllMeals() {
        return this.mealRepository.findAll();
    }

    @Override
    public Page<Meal> getMeals(int page, int size) {
        return this.mealRepository.findAllByDeletedFalse(PageRequest.of(page, size, Sort.Direction.DESC, "id"));
    }

    @Override
    public Page<Meal> getMeals(String search, int page, int size) {
        return this.mealRepository.searchMeals(PageRequest.of(page, size, Sort.Direction.DESC, "id"), search.trim().toLowerCase());
    }

    @Override
    public Page<Meal> getAllMeals(String search, int page, int size) {
        return this.mealRepository.searchAllMeals(PageRequest.of(page, size, Sort.Direction.DESC, "id"), search.trim().toLowerCase());
    }

    /**
     * Get popular meals list
     *
     * @return List<Meal>
     */
    @Override
    public List<Meal> getPopularMeals(int page, int size) {
        return this.mealRepository.findTop10ByDeletedFalseOrderByViewsDesc(PageRequest.of(page, size, Sort.Direction.DESC, "id")).getContent();
    }

    @Override
    public List<Meal> getUserPreferredMeals(String email) {
        return this.mealRepository.findMealByUsersPreferences_EmailAndDeletedFalse(email);
    }
}
