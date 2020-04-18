package org.restaurant.salado.controllers;

import org.restaurant.salado.entities.MealOrder;
import org.restaurant.salado.entities.Order;
import org.restaurant.salado.entities.User;
import org.restaurant.salado.facades.IAuthenticationFacade;
import org.restaurant.salado.models.MealOrderRequest;
import org.restaurant.salado.services.MealOrderService;
import org.restaurant.salado.services.MealService;
import org.restaurant.salado.services.OrderService;
import org.restaurant.salado.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/mealorders")
@CrossOrigin(value = "*")
@Transactional
public class MealOrderRestController {

    private static final String ERROR = "error";
    private static final String STATUS = "status";
    private static final String MESSAGE = "message";
    private UserService userService;
    private OrderService orderService;
    private MealService mealService;
    private MealOrderService mealOrderService;
    private IAuthenticationFacade authenticationFacade;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setMealService(MealService mealService) {
        this.mealService = mealService;
    }

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
    public ResponseEntity<Map<String, Object>> addUserOder(@RequestBody MealOrderRequest mealOrderRequest) {
        try {
            // Response data
            Map<String, Object> responseData = new HashMap<>();
            // Create MealOrder object
            MealOrder mealOrder = new MealOrder(null, null, this.mealService.getMeal(mealOrderRequest.getMealId()), mealOrderRequest.getQuantity(), BigDecimal.ZERO);
            // Fetch connected user from database
            User user = this.userService.getUser(this.authenticationFacade.getAuthentication().getName());
            // Get or create user order cart
            List<Order> userOrders = this.orderService.getUserOrders(user.getUserId().getId());
            // Get last order
            Optional<Order> optionalOrder = userOrders.stream().filter(order -> !order.isCancelled() && !order.isDelivered()).findFirst();
            // Create a new user order
            // Check if user has already a waiting order
            Order userOrder = optionalOrder.orElseGet(() -> new Order(null, user, null, BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(0), "", new Date(), false, false, null));
            // Check if meal already exists in cart or meal stock is empty
            if (userOrder.getMealOrders() != null) {
                Stream<MealOrder> stream = userOrder.getMealOrders().stream();
                if (stream.anyMatch(ml -> ml.getMeal().getId().equals(mealOrder.getMeal().getId()))) {
                    responseData.put(ERROR, true);
                    responseData.put(MESSAGE, "Meal already exists in your cart!");
                    return ResponseEntity.ok(responseData);
                }
            }
            // Check if meal stock is available
            if (mealOrder.getMeal().getStock() == 0) {
                responseData.put(ERROR, true);
                responseData.put(MESSAGE, "No stock available for the selected meal!");
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
            responseData.put(ERROR, false);
            responseData.put(MESSAGE, "Meal has been added to your cart successfully");
            return ResponseEntity.ok(responseData);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

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
            // Retrieve Order And MealOrder
            MealOrder mealOrder = this.mealOrderService.getMealOrder(mealOrderId);
            // Delete meal order
            boolean isMealOrderDeleted = this.mealOrderService.deleteMealOrder(mealOrderId);
            // Retrieve order
            Order order = mealOrder.getOrder();
            // Check if meal order is delete
            if (isMealOrderDeleted) {
                // Set order as to set it as cancelled if no mealOrder remain
                if (order.getMealOrders().isEmpty()) {
                    // Set order as cancelled
                    order.setCancelled(true);
                    this.orderService.saveOrder(order);
                }
                data.put(STATUS, true);
                data.put(MESSAGE, "Meal order has been deleted from your cart successfully");
            } else {
                throw new InternalError("An error occurred while deleting meal order, please try again!");
            }
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
                    data.put(STATUS, true);
                    data.put(MESSAGE, "Meal order quantity has been updated successfully");
                } else {
                    data.put(STATUS, false);
                    data.put(MESSAGE, "Unavailable quantity in the stock, only " + mealOrder.getMeal().getStock() + " is available");
                }
            } else {
                throw new InternalError("An error occurred while updating meal order quantity, please try again!");
            }
        } catch (Exception ex) {
            data.put(STATUS, false);
            data.put(MESSAGE, ex.getMessage());
        }
        // return response
        return ResponseEntity.ok(data);
    }


}
