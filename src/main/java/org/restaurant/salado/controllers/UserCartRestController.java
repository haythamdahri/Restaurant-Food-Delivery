package org.restaurant.salado.controllers;

import org.restaurant.salado.entities.Order;
import org.restaurant.salado.entities.User;
import org.restaurant.salado.services.OrderService;
import org.restaurant.salado.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/usercart")
@CrossOrigin(value = "*")
public class UserCartRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    /*
     * User cart
     * @temp using user with id=1
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> getUserCart(Authentication authentication) {
        // Fetch connected user from database
        User user = this.userService.getUser(authentication.getName());
        // Create results data
        Map<Object, Object> data = new HashMap<>();
        // Get user orders
        Collection<Order> orders = this.orderService.getUserOrders(user.getId());
        // Get last active order
        Order userActiveOrder = this.orderService.getLastActiveOrder(user.getId());
        // Check if their is an active order
        if (userActiveOrder == null) {
            data.put("status", true);
            data.put("noActiveOrder", true);
        } else {
            userActiveOrder.calculateTotalPrice();
            userActiveOrder = this.orderService.saveOrder(userActiveOrder);
            data.put("status", true);
            data.put("activeOrder", userActiveOrder);
        }
        return new ResponseEntity<Map<Object, Object>>(data, HttpStatus.OK);
    }

}
