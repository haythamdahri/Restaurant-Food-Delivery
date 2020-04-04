package org.restaurant.salado.controllers;

import org.restaurant.salado.entities.Meal;
import org.restaurant.salado.services.MealService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private MealService mealService;

    /**
     * Retrieve all meals Endpoint
     *
     * @return ResponseEntity<List < Meal>>
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<Meal>> getAllMealsEndPoint() {
        return new ResponseEntity<>(this.mealService.getMeals(), HttpStatus.OK);
    }

    /**
     * Meal post request handler
     *
     * @param meal
     * @return ResponseEntity<Meal>
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Meal> postMeal(@RequestBody Meal meal) {
        meal = this.mealService.saveMeal(meal);
        return new ResponseEntity<>(meal, HttpStatus.OK);
    }

}
