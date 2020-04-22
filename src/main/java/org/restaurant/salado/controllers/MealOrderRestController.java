package org.restaurant.salado.controllers;

import org.restaurant.salado.facades.IAuthenticationFacade;
import org.restaurant.salado.models.MealOrderRequest;
import org.restaurant.salado.providers.Constants;
import org.restaurant.salado.services.MealOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/mealorders")
@CrossOrigin(value = "*")
@Transactional
public class MealOrderRestController {

    private static final String STATUS = "status";
    private static final String MESSAGE = "message";
    private MealOrderService mealOrderService;
    private IAuthenticationFacade authenticationFacade;

    @Autowired
    public void setMealOrderService(MealOrderService mealOrderService) {
        this.mealOrderService = mealOrderService;
    }

    @Autowired
    public void setAuthenticationFacade(IAuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    /**
     * Add meal order to user orders
     *
     * @param mealOrderRequest: MealOrderRequest Object Sent From Client Side
     * @return ResponseEntity<Map < String, Object>>
     */
    @PostMapping(path = "/")
    public ResponseEntity<Map<String, Object>> addMealToUserOder(@RequestBody MealOrderRequest mealOrderRequest) {
        // Response data
        Map<String, Object> data = new HashMap<>();
        try {
            //  Use service tier to add meal to user order
            this.mealOrderService.createOrUpdateUserCurrentOrder(this.authenticationFacade.getAuthentication().getName(), mealOrderRequest);
            // A BusinessException will be thrown in case of error
            data.put(STATUS, true);
            data.put(MESSAGE, Constants.MEAL_ADDED_TO_CART_SUCCESSFULLY);
        } catch (Exception ex) {
            // Error response data
            data.put(STATUS, false);
            data.put(MESSAGE, ex.getMessage());
        }
        // Return response data
        return ResponseEntity.ok(data);

    }

    /**
     * Delete meal from a mealOrder object
     *
     * @param mealOrderId: Meal Order Identifier
     * @return ResponseEntity<Map < Object, Object>>
     */
    @DeleteMapping(path = "/{mealOrderId}")
    public ResponseEntity<Map<Object, Object>> deleteMealOrder(@PathVariable(value = "mealOrderId") Long mealOrderId) {
        // Create response object
        Map<Object, Object> data = new HashMap<>();
        try {
            // Delete meal order from user cart
            this.mealOrderService.deleteMealOrder(mealOrderId);
            data.put(STATUS, true);
            data.put(MESSAGE, Constants.MEAL_ORDER_QUANTITY_UPDATED_SUCCESSFULLY);
        } catch (Exception ex) {
            data.put(STATUS, false);
            data.put(MESSAGE, ex.getMessage());
        }
        // return response
        return ResponseEntity.ok(data);
    }

    /**
     * Update mealOrder quantity
     *
     * @param mealOrderId: Meal Order Identifier
     * @param quantity:    Meal Order Quantity
     * @return ResponseEntity
     */
    @PatchMapping(path = "/{mealOrderId}/quantity/{quantity}")
    public ResponseEntity<Map<Object, Object>> updateMealOrderQuantity(@PathVariable(value = "mealOrderId") Long mealOrderId, @PathVariable(value = "quantity") int quantity) {
        // Create response object
        Map<Object, Object> data = new HashMap<>();
        try {
            // Update mealOrder quantity using service tier
            this.mealOrderService.updateMealOrderQuantity(mealOrderId, quantity);
            // Put success data
            data.put(STATUS, true);
            data.put(MESSAGE, "Meal order quantity has been updated successfully");
        } catch (Exception ex) {
            data.put(STATUS, false);
            data.put(MESSAGE, ex.getMessage());
        }
        // return response
        return ResponseEntity.ok(data);
    }


}
