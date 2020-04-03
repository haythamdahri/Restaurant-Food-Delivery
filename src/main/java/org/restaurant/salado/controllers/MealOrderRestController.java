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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

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

    /*
     * Add meal order to user orders
     * @temp set orders for first user
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
        if (optionalOrder.isPresent()) {
            userOrder = optionalOrder.get();
        } else {
            userOrder = new Order(null, user, null, 0, "", new Date(), false, false, null);
        }
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
        userOrder.calculateTotalPrice();
        userOrder = this.orderService.saveOrder(userOrder);
        // Calculate price then save mealOrder
        mealOrder.setPrice(mealOrder.getQuantity() * mealOrder.getMeal().getPrice());
        mealOrder.setOrder(userOrder);
        this.mealOrderService.saveMealOrder(mealOrder);
        // Return response entity
        return new ResponseEntity<>(userOrder, HttpStatus.OK);
    }

}
