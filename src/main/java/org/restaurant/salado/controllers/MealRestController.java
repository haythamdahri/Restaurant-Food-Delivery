package org.restaurant.salado.controllers;

import org.restaurant.salado.entities.Meal;
import org.restaurant.salado.providers.Constants;
import org.restaurant.salado.services.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<Page<Meal>> retrieveMealsEndPoint(@RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "${page.default_size}") int size) throws InterruptedException {
        return new ResponseEntity<>(this.mealService.getMeals(page, size), HttpStatus.OK);

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

    /**
     * Meal post, put request handler
     *
     * @param meal
     * @return ResponseEntity<Meal>
     */
    @RequestMapping(value = "/", method = {RequestMethod.POST, RequestMethod.PATCH})
    public ResponseEntity<Meal> postMeal(@RequestBody Meal meal) {
        meal = this.mealService.saveMeal(meal);
        return new ResponseEntity<>(meal, HttpStatus.OK);
    }

}
