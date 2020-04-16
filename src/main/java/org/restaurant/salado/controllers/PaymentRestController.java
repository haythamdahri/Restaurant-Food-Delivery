package org.restaurant.salado.controllers;

import com.stripe.model.Charge;
import org.restaurant.salado.entities.Currency;
import org.restaurant.salado.entities.Order;
import org.restaurant.salado.entities.User;
import org.restaurant.salado.facades.IAuthenticationFacede;
import org.restaurant.salado.services.OrderService;
import org.restaurant.salado.services.ChargeService;
import org.restaurant.salado.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/payments")
@Transactional
public class PaymentRestController {

    @Value("${STRIPE_PUBLIC_KEY}")
    private String stripePublicKey;

    @Autowired
    @Qualifier(value = "stripeChargeServiceImpl")
    private ChargeService chargeService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private IAuthenticationFacede iAuthenticationFacede;

    /**
     * Payment checkout GET endpoint
     *
     * @param authentication
     * @return ResponseEntity
     * @throws Exception
     */
    @RequestMapping(path = "/checkout", method = RequestMethod.GET)
    public ResponseEntity<?> checkout(@AuthenticationPrincipal Authentication authentication) throws Exception {
        Map<String, Object> data = new HashMap<>();
        // Check if user is authenticated
        if (authentication != null) {
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
                data.put("order", userActiveOrder);
            }
            // Return Response
            return ResponseEntity.ok(data);
        }
        // Throw un authenticated exception
        throw new Exception("User not authenticated");
    }

    @RequestMapping(path = "/charge", method = RequestMethod.POST)
    public ResponseEntity<?> chargeCard(@RequestHeader Map<String, String> headers, @AuthenticationPrincipal Authentication authentication) throws Exception {
        try {
            Map<String, Object> data = new HashMap<>();
            // Retrieve current user
            User user = this.userService.getUser(authentication.getName());
            // Retrieve last order
            Order userActiveOrder = this.orderService.getLastActiveOrder(user.getId());
            // Check if there is an order in place
            if (userActiveOrder != null) {
                BigDecimal amount = userActiveOrder.getTotalPrice();
            } else {
                // Return no order in progress
                data.put("status", false);
                data.put("noActiveOrder", false);
                return ResponseEntity.ok(data);
            }
            // Create charge
            String token = headers.get("token");
            Charge charge = this.chargeService.chargeCreditCard(token, userActiveOrder.getTotalPrice(), user);
            // Set successful data of successful transaction
            data.put("status", true);
            data.put("message", "Your order has been purchased successfully");
            data.put("order", this.orderService.getLastActiveOrder(user.getId()));
            // Return response
            return ResponseEntity.ok(data);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

    }
}
