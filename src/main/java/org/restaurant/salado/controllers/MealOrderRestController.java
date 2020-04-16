package org.restaurant.salado.controllers;

import org.restaurant.salado.entities.MealOrder;
import org.restaurant.salado.entities.Order;
import org.restaurant.salado.entities.User;
import org.restaurant.salado.models.MealOrderRequest;
import org.restaurant.salado.services.MealOrderService;
import org.restaurant.salado.services.MealService;
import org.restaurant.salado.services.OrderService;
import org.restaurant.salado.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/mealorders")
@CrossOrigin(value = "*")
@Transactional
public class MealOrderRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MealService mealService;

    @Autowired
    private MealOrderService mealOrderService;

    /**
     * Add meal order to user orders
     *
     * @param mealOrderRequest
     * @param authentication
     * @return ResponseEntity
     */
    @RequestMapping(path = "/", method = RequestMethod.POST)
    public ResponseEntity<Object> addUserOder(@RequestBody MealOrderRequest mealOrderRequest, Authentication authentication) throws InterruptedException {
       try {
           // Response data
           Map<String, Object> responseData = new HashMap<>();
           // Create MealOrder object
           MealOrder mealOrder = new MealOrder(null, null, this.mealService.getMeal(mealOrderRequest.getMealId()), mealOrderRequest.getQuantity(), BigDecimal.ZERO);
           // Fetch connected user from database
           User user = this.userService.getUser(authentication.getName());
           // Get or create user order cart
           List<Order> userOrders = this.orderService.getUserOrders(user.getId());
           // Get last order
           Optional<Order> optionalOrder = userOrders.stream().filter(order -> !order.isCancelled() && !order.isDelivered()).findFirst();
           // Create a new user order
           // Check if user has already a waiting order
           Order userOrder = optionalOrder.orElseGet(() -> new Order(null, user, null, BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(0), "", new Date(), false, false, null));
           // Check if meal already exists in cart or meal stock is empty
           if (userOrder.getMealOrders() != null) {
               if( userOrder.getMealOrders().stream().anyMatch(ml -> ml.getMeal().getId().equals(mealOrder.getMeal().getId())) ) {
                   responseData.put("error", true);
                   responseData.put("message", "Meal already exists in your cart!");
                   return ResponseEntity.ok(responseData);
               }
           }
           // Check if meal stock is available
           if( mealOrder.getMeal().getStock() == 0 ) {
               responseData.put("error", true);
               responseData.put("message", "No stock available for the selected meal!");
               return ResponseEntity.ok(responseData);
           }
           // Save mealOrder
           mealOrder.setOrder(userOrder);
           this.mealOrderService.saveMealOrder(mealOrder);
           // Save order in case everything is ok
           userOrder.setCancelled(false);
           userOrder.setDelivered(false);
           this.orderService.saveOrder(userOrder);
           // Build success response data ned Return response entity
           responseData.put("error", false);
           responseData.put("message", "Meal has been added to your cart successfully");
           return ResponseEntity.ok(responseData);
       } catch(Exception ex) {
           ex.printStackTrace();
           return null;
       }

    }

    /**
     * Delete meal from a mealOrder object
     *
     * @param mealOrderId
     * @param authentication
     * @return ResponseEntity
     * @throws Exception
     */
    @RequestMapping(path = "/{mealOrderId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMealOrder(@PathVariable(value = "mealOrderId") Long mealOrderId, Authentication authentication) throws Exception {
        // Create response object
        Map<Object, Object> data = new HashMap<>();
        try {
            // Retrieve Order And MealOrder
            MealOrder mealOrder = this.mealOrderService.getMealOrder(mealOrderId);
            // Delete meal order
            boolean isMealOrderDeleted = this.mealOrderService.deleteMealOrder(mealOrderId);
            // Retrieve order
            Order order = mealOrder.getOrder();
            // Check if meal order is delete
            if (isMealOrderDeleted) {
                // Set order as to set it as cancelled if no mealOrder remain
                if( order.getMealOrders().isEmpty() ) {
                    // Set order as cancelled
                    order.setCancelled(true);
                    this.orderService.saveOrder(order);
                }
                data.put("status", true);
                data.put("message", "Meal order has been deleted from your cart successfully");
            } else {
                throw new Exception();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            data.put("status", false);
            data.put("message", "An error occurred while deleting meal order, please try again!");
        }
        // return response
        return new ResponseEntity<Map<Object, Object>>(data, HttpStatus.OK);
    }

    /**
     * Update mealOrder quantity
     *
     * @param mealOrderId
     * @param quantity
     * @return ResponseEntity
     */
    @RequestMapping(path = "/{mealOrderId}/quantity/{quantity}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateMealOrderQuantity(@PathVariable(value = "mealOrderId") Long mealOrderId, @PathVariable(value = "quantity") int quantity) {
        // Create response object
        Map<Object, Object> data = new HashMap<>();
        try {
            // Retrieve mealOrder
            MealOrder mealOrder = this.mealOrderService.getMealOrder(mealOrderId);
            // Check if meal order exists
            if (mealOrder != null) {
                // Check meal available quantity
                if (mealOrder.getMeal().getStock() >= quantity) {
                    // Update mealOrder quantity and save it
                    mealOrder.setQuantity(quantity);
                    this.mealOrderService.saveMealOrder(mealOrder);
                    // Set response data
                    data.put("status", true);
                    data.put("message", "Meal order quantity has been updated successfully");
                } else {
                    data.put("status", false);
                    data.put("message", "Unavailable quantity in the stock, only " + mealOrder.getMeal().getStock() + " is available");
                }
            } else {
                throw new Exception();
            }
        } catch (Exception ex) {
            data.put("status", false);
            data.put("message", "An error occurred while updating meal order quantity, please try again!");
        }
        // return response
        return new ResponseEntity<Map<Object, Object>>(data, HttpStatus.OK);
    }


}
