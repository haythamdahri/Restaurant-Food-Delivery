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

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Haytam DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/usercart")
@CrossOrigin(value = "*")
@Transactional
public class UserCartRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    /**
     * User cart
     *
     * @param authentication
     * @return ResponseEntity
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getUserCart(Authentication authentication) {
        // Fetch connected user from database
        User user = this.userService.getUser(authentication.getName());
        // Create results data
        Map<String, Object> data = new HashMap<>();
        // Get last active order
        Order userActiveOrder = this.orderService.getLastActiveOrder(user.getId());
        // Check if their is an active order
        if (userActiveOrder == null || userActiveOrder.getMealOrders().isEmpty()) {
            data.put("status", true);
            data.put("activeOrder", null);
            data.put("noActiveOrder", true);
        } else {
            data.put("status", true);
            data.put("activeOrder", userActiveOrder);
            data.put("noActiveOrder", false);
        }
        return ResponseEntity.ok(data);
    }

}
