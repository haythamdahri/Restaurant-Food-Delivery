package org.restaurant.salado.controllers;

import org.restaurant.salado.entities.Meal;
import org.restaurant.salado.entities.Order;
import org.restaurant.salado.services.MealService;
import org.restaurant.salado.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/orders")
@CrossOrigin(value = "*")
public class OrderRestController {

    @Autowired
    private OrderService orderService;

    /**
     * Retrieve authenticated user order Endpoint
     *
     * @return ResponseEntity<List <Order>>
     */
    @RequestMapping(value = "/authuser", method = RequestMethod.GET)
    public ResponseEntity<List<Order>> getUserOrdersEndPoint(@AuthenticationPrincipal Authentication authentication) {
        String email = authentication.getName();
        return new ResponseEntity<>(this.orderService.getUserOrders(email), HttpStatus.OK);
    }

    /**
     * Retrieve a given user email orders Endpoint
     *
     * @return ResponseEntity<List < Meal>>
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<Order>> getUserOrdersEndPoint(@RequestParam(value = "email") String email) {
        return new ResponseEntity<>(this.orderService.getUserOrders(email), HttpStatus.OK);
    }

}
