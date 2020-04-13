package org.restaurant.salado.controllers;

import org.restaurant.salado.entities.Currency;
import org.restaurant.salado.entities.Order;
import org.restaurant.salado.services.OrderService;
import org.restaurant.salado.services.PaymentService;
import org.restaurant.salado.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/payments")
@Transactional
public class PaymentController {

    @Value("${STRIPE_PUBLIC_KEY}")
    private String stripePublicKey;

    @Autowired
    @Qualifier(value = "stripePaymentServiceImpl")
    private PaymentService paymentService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    /**
     * Payment checkout GET endpoint
     * @param authentication
     * @return ResponseEntity
     * @throws Exception
     */
    @RequestMapping(path = "/checkout", method = RequestMethod.GET)
    public ResponseEntity<?> checkout(@AuthenticationPrincipal Authentication authentication) throws Exception{
        Thread.sleep(4500);
        Map<String, Object> data = new HashMap<>();
        // Check if user is authenticated
        if( authentication != null ) {
            // Get last active order
            Order userActiveOrder = this.orderService.getLastActiveOrder(this.userService.getUser(authentication.getName()).getId());
            // Check if an order is in progress
            if (userActiveOrder == null || userActiveOrder.getMealOrders().isEmpty()) {
                data.put("status", true);
                data.put("noActiveOrder", true);
            } else {
                data.put("amount", userActiveOrder.getTotalPrice()); // in cents
                data.put("stripePublicKey", stripePublicKey);
                data.put("currency", Currency.MAD);
                data.put("status", true);
                data.put("noActiveOrder", false);
            }
            // Return Response
            return ResponseEntity.ok(data);
        }
        // Throw un authenticated exception
        throw new Exception("User not authenticated");
    }

}
