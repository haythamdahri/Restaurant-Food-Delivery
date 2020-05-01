package org.restaurant.salado.controllers;

import org.restaurant.salado.entities.Order;
import org.restaurant.salado.entities.User;
import org.restaurant.salado.facades.IAuthenticationFacade;
import org.restaurant.salado.services.OrderService;
import org.restaurant.salado.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    private UserService userService;

    private OrderService orderService;

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
    public void setAuthenticationFacade(IAuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    /**
     * User carT
     *
     * @return ResponseEntity
     */
    @GetMapping(value = "/")
    public ResponseEntity<Map<String, Object>> getUserCart() {
        // Fetch connected user from database
        User user = this.userService.getUser(this.authenticationFacade.getAuthentication().getName());
        // Create results data
        Map<String, Object> data = new HashMap<>();
        // Get last active order And Delete meals from user cart if deleted
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
        // Return response
        return ResponseEntity.ok(data);
    }

}
