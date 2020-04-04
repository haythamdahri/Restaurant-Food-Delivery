package org.restaurant.salado.controllers;

import org.restaurant.salado.entities.MealOrder;
import org.restaurant.salado.entities.Order;
import org.restaurant.salado.entities.User;
import org.restaurant.salado.services.MealOrderService;
import org.restaurant.salado.services.OrderService;
import org.restaurant.salado.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/mealorders")
@CrossOrigin(value = "*")
public class MealOrderRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MealOrderService mealOrderService;

    /**
     * Add meal order to user orders
     *
     * @param mealOrder
     * @param authentication
     * @return ResponseEntity
     */
    @RequestMapping("/")
    public ResponseEntity<Object> addUserOder(@RequestBody MealOrder mealOrder, Authentication authentication) {
        // Fetch connected user from database
        User user = this.userService.getUser(authentication.getName());
        // Get or create user order cart
        Collection<Order> userOrders = this.orderService.getUserOrders(user.getId());
        // Get last order
        Optional<Order> optionalOrder = userOrders.stream().filter(order -> !order.isCancelled() && !order.isDelivered()).findFirst();
        // Create a new user order
        Order userOrder;
        // Check if user has already a waiting order
        userOrder = optionalOrder.orElseGet(() -> new Order(null, user, null, BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(0), "", new Date(), false, false, null));
        // Check if meal already exists in cart
        if (userOrder.getMealOrders() != null) {
            for (MealOrder ml : userOrder.getMealOrders()) {
                if (ml.getMeal().getId() == mealOrder.getMeal().getId()) {
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("message", "Meal already exists in your cart!");
                    errorResponse.put("error", "true");
                    return new ResponseEntity<>(errorResponse, HttpStatus.OK);
                }
            }
        }
        // Save order
        userOrder.setCancelled(false);
        userOrder.setDelivered(false);
        userOrder = this.orderService.saveOrder(userOrder);
        // Calculate price then save mealOrder
        BigDecimal totalPrice = BigDecimal.valueOf(mealOrder.getQuantity()).multiply(mealOrder.getMeal().getPrice());
        mealOrder.setTotalPrice(totalPrice);
        mealOrder.setOrder(userOrder);
        this.mealOrderService.saveMealOrder(mealOrder);
        // Return response entity
        return new ResponseEntity<>(userOrder, HttpStatus.OK);
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
            // Delete meal order
            boolean isMealOrderDeleted = this.mealOrderService.deleteMealOrder(mealOrderId);
            // Check if meal order is delete
            if (isMealOrderDeleted) {
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
