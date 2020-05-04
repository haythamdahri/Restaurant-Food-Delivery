package org.restaurant.salado.controllers;

import javassist.NotFoundException;
import org.restaurant.salado.entities.Meal;
import org.restaurant.salado.facades.IAuthenticationFacade;
import org.restaurant.salado.models.MealRequest;
import org.restaurant.salado.services.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
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

    private MealService mealService;

    private IAuthenticationFacade authenticationFacade;

    @Autowired
    public void setMealService(MealService mealService) {
        this.mealService = mealService;
    }

    @Autowired
    public void setAuthenticationFacade(IAuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    /**
     * Retrieve meals Page Endpoint
     *
     * @return ResponseEntity<Page < Meal>>
     */
    @GetMapping(path = "/")
    public ResponseEntity<Page<Meal>> retrieveMealsEndPoint(@RequestParam(value = "search", required = false, defaultValue = "") String search, @RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "${page.default-size}") int size) {
        return ResponseEntity.ok(this.mealService.getMeals(search, page, size));
    }

    /**
     * Save Meal
     *
     * @param mealRequest: MealRequest
     * @return ResponseEntity<Meal>
     */
    @PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Meal> saveMealEndPoint(@Valid @ModelAttribute MealRequest mealRequest) throws IOException {
        return ResponseEntity.ok(this.mealService.saveMeal(mealRequest));
    }

    /**
     * Retrieve meals Page Endpoint
     * Get all meals: DELETED AND NON DELETED ones
     *
     * @return ResponseEntity<Page < Meal>>
     */
    @PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/page")
    public ResponseEntity<Page<Meal>> retrieveAllMealsPageEndPoint(@RequestParam(value = "search", required = false, defaultValue = "") String search, @RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "${page.default-size}") int size) {
        return ResponseEntity.ok(this.mealService.getAllMeals(search, page, size));
    }

    /**
     * Retrieve all meals Endpoint
     * Authorize Only Employees And Admins
     *
     * @return ResponseEntity<List < Meal>>
     */
    @PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/all")
    public ResponseEntity<List<Meal>> fetchMealsEndPoint() {
        return ResponseEntity.ok(this.mealService.getMeals());
    }


    /**
     * Retrieve A Meal For Administration
     *
     * @param id: Meal Id
     * @return ResponseEntity
     */
    @GetMapping(value = "/all/{id}")
    public ResponseEntity<Meal> retrieveMealForBackOffice(@PathVariable(value = "id") Long id) {
        Meal meal = this.mealService.getMeal(id);
        if (meal != null) {
            return ResponseEntity.ok(meal);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Retrieve meal and increment number of views
     *
     * @param id: Meal Id
     * @return ResponseEntity
     */
    @GetMapping(value = "/{id}")
    @Transactional
    public ResponseEntity<Map<String, Object>> fetchMeal(@PathVariable(value = "id") Long id) {
        // Build response object
        Map<String, Object> data = new HashMap<>();
        Meal meal;
        boolean mealPreferred = false;
        try {
            // Retrieve meal
            meal = this.mealService.getExistingMeal(id);
            // Check if meal exists
            if (meal != null) {
                // Update number of views and save the meal
                meal = this.mealService.saveMeal(meal.incrementViews());
                // Check meal preferences if user is authenticated
                if (this.authenticationFacade.getAuthentication() != null) {
                    mealPreferred = meal.getUsersPreferences().stream().anyMatch(user -> user.getEmail().equalsIgnoreCase(this.authenticationFacade.getAuthentication().getName()));
                }
                // Set response object
                data.put("meal", meal);
                data.put("mealPreferred", mealPreferred);
                // Return 200 response
                return ResponseEntity.ok(data);
            }
            // throw exception
            throw new NotFoundException("No meal has been found!");
        } catch (Exception ex) {
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
    @GetMapping(value = "/popular")
    public ResponseEntity<List<Meal>> getPopularMealsEndpoint(@RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "${page.default-size}") int size) {
        return new ResponseEntity<>(this.mealService.getPopularMeals(page, size), HttpStatus.OK);
    }

    /**
     * Delete a given Meal By ID
     * Deleting consist on set field deleted to true only without physical delete
     * Authorize Only Employees And Admins
     *
     * @param id: Meal IDENTIFIER
     * @return ResponseEntity<?>
     */
    @PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteMealEndPoint(@PathVariable(value = "id") Long id) {
        try {
            return ResponseEntity.ok(this.mealService.deleteMeal(id));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    /**
     * Undo Delete of a given Meal By ID
     * Deleting consist on set field deleted to true only without physical delete
     * Authorize Only Employees And Admins
     *
     * @param id: Meal IDENTIFIER
     * @return ResponseEntity<?>
     */
    @PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/{id}/undo")
    public ResponseEntity<?> resetMealEndPoint(@PathVariable(value = "id") Long id) {
        try {
            return ResponseEntity.ok(this.mealService.undoMealDelete(id));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

}
