package org.restaurant.salado.controllers;

import org.restaurant.salado.entities.Order;
import org.restaurant.salado.facades.IAuthenticationFacade;
import org.restaurant.salado.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/orders")
@CrossOrigin(value = "*")
public class OrderRestController {

    private OrderService orderService;

    private IAuthenticationFacade authenticationFacade;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setAuthenticationFacade(IAuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    /**
     * Retrieve all orders
     * Authorize Only Employees And Admins
     *
     * @return ResponseEntity<List < Order>>
     */
    @PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/all")
    public ResponseEntity<List<Order>> retrieveAllOrders() {
        return ResponseEntity.ok(this.orderService.getOrders());
    }

    /**
     * Retrieve authenticated user order Endpoint
     *
     * @return ResponseEntity<List < Order>>
     */
    @GetMapping(value = "/authuser")
    public ResponseEntity<List<Order>> getUserOrdersEndPoint() {
        String email = this.authenticationFacade.getAuthentication().getName();
        return new ResponseEntity<>(this.orderService.getUserOrders(email), HttpStatus.OK);
    }

    /**
     * Retrieve a given user email orders Endpoint
     *
     * @return ResponseEntity<List < Meal>>
     */
    @GetMapping(value = "/")
    public ResponseEntity<List<Order>> getUserOrdersEndPoint(@RequestParam(value = "email") String email) {
        return new ResponseEntity<>(this.orderService.getUserOrders(email), HttpStatus.OK);
    }

}
