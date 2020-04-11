package org.restaurant.salado.controllers;

import org.restaurant.salado.entities.Meal;
import org.restaurant.salado.models.MealOrderRequest;
import org.restaurant.salado.providers.Constants;
import org.restaurant.salado.services.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/meals")
@CrossOrigin(value = "*")
public class MealRestController {

    private static final String DEFAULT_PAGE_SIZE = String.valueOf(Constants.DEFAULT_PAGE_SIZE);
    @Autowired
    private MealService mealService;

    /**
     * Retrieve all meals Endpoint
     *
     * @return ResponseEntity<List < Meal>>
     */
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseEntity<List<Meal>> fetchMealsEndPoint(@RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "${page.default_size}") int size) throws InterruptedException {
        return ResponseEntity.ok(this.mealService.getMeals(page, size).getContent());
    }

    /**
     * Retrieve all meals Endpoint
     *
     * @return ResponseEntity<Page<Meal>>
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<Page<Meal>> retrieveMealsEndPoint(@RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "${page.default_size}") int size) throws InterruptedException {
        return ResponseEntity.ok(this.mealService.getMeals(page, size));
    }

    /**
     * Meal post, put request handler
     *
     * @param mealRequest
     * @return ResponseEntity<Meal>
     */
    @RequestMapping(value = "/", method = {RequestMethod.POST, RequestMethod.PATCH})
    public ResponseEntity<?> postMeal(@RequestBody MealOrderRequest mealRequest) {
        System.out.println("POSTING -----------> MEAL");
        System.out.println(mealRequest);
        return new ResponseEntity<>("Great", HttpStatus.OK);
    }

    /**
     * Retrieve meal and increment number of views
     *
     * @param id
     * @return ResponseEntity
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @Transactional
    public ResponseEntity<?> fetchMeal(@PathVariable(value = "id") Long id, @AuthenticationPrincipal Authentication authentication) {
        // Build response object
        Map<Object, Object> data = new HashMap<>();
        Meal meal = null;
        boolean mealPreferred = false;
        try {
            // Retrieve meal
            meal = this.mealService.getMeal(id);
            // Check if meal exists
            if( meal != null ) {
                // Update number of views and save the meal
                meal = this.mealService.saveMeal(meal.incrementViews());
                // Check meal preferences if user is authenticated
                if( authentication != null ) {
                    mealPreferred = meal.getUsersPreferences().stream().anyMatch(user -> {
                        return user.getEmail().equalsIgnoreCase(authentication.getName());
                    });
                }
                // Set response object
                data.put("meal", meal);
                data.put("mealPreferred", mealPreferred);
                // Return 200 response
                return ResponseEntity.ok(data);
            }
            // throw exception
            throw new Exception("No meal has been found!");
        } catch(Exception ex) {
            ex.printStackTrace();
            data.put("message", ex.getMessage());
            // Return 500 response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(data);
        }
    }

    /**
     * Retrieve popular meals Endpoint
     *
     * @return ResponseEntity<List < Meal>>
     */
    @RequestMapping(value = "/popular", method = RequestMethod.GET)
    public ResponseEntity<List<Meal>> getPopularMealsEndpoint(@RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "${page.default_size}") int size) {
        return new ResponseEntity<>(this.mealService.getPopularMeals(page, size), HttpStatus.OK);
    }


}
