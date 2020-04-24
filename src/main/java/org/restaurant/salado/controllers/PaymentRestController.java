package org.restaurant.salado.controllers;

import org.restaurant.salado.dtos.ShippingDTO;
import org.restaurant.salado.entities.Currency;
import org.restaurant.salado.entities.Order;
import org.restaurant.salado.entities.Payment;
import org.restaurant.salado.facades.IAuthenticationFacade;
import org.restaurant.salado.services.ChargeService;
import org.restaurant.salado.services.OrderService;
import org.restaurant.salado.services.PaymentService;
import org.restaurant.salado.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/payments")
@Transactional
public class PaymentRestController {

    private static final String STATUS = "status";
    private static final String MESSAGE = "message";
    private static final String PAYMENT = "payment";
    private static final String NO_ACTIVE_ORDER = "noActiveOrder";

    @Value("${STRIPE_PUBLIC_KEY}")
    private String stripePublicKey;

    private ChargeService chargeService;

    private OrderService orderService;

    private UserService userService;

    private PaymentService paymentService;

    private IAuthenticationFacade authenticationFacade;

    @Autowired
    public void setChargeService(@Qualifier(value = "stripeChargeServiceImpl") ChargeService chargeService) {
        this.chargeService = chargeService;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Autowired
    public void setAuthenticationFacade(IAuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    /**
     * Payment checkout GET endpoint
     *
     * @return ResponseEntity<Map < String, Object>>
     */
    @GetMapping(path = "/checkout")
    public ResponseEntity<Map<String, Object>> checkout() {
        Map<String, Object> data = new HashMap<>();
        // Get last active order
        Order userActiveOrder = this.orderService.getLastActiveOrder(this.userService.getUser(this.authenticationFacade.getAuthentication().getName()).getId());
        // Check if an order is in progress
        if (userActiveOrder.getMealOrders().isEmpty()) {
            data.put(STATUS, true);
            data.put(NO_ACTIVE_ORDER, true);
        } else {
            data.put("amount", userActiveOrder.getTotalPrice()); // in cents
            data.put("stripePublicKey", stripePublicKey);
            data.put("currency", Currency.MAD);
            data.put(STATUS, true);
            data.put(NO_ACTIVE_ORDER, false);
            data.put("order", userActiveOrder);
        }
        // Return Response
        return ResponseEntity.ok(data);
    }

    /**
     * Submit an order charge for the current authenticated user
     *
     * @param headers: HttpHeader Map
     * @return ResponseEntity<Map < String, Object>>
     */
    @PostMapping(path = "/charge")
    public ResponseEntity<Map<String, Object>> chargeCard(@RequestHeader Map<String, String> headers, @RequestBody ShippingDTO shippingDTO) {
        Map<String, Object> data = new HashMap<>();
        try {
            // Create charge
            String token = headers.get("token");
            // Charge credit and wait until complete to get the charge
            this.chargeService.chargeCreditCard(token, this.authenticationFacade.getAuthentication().getName(), shippingDTO).join();
            // Set successful data of successful transaction
            data.put(STATUS, true);
            data.put(MESSAGE, "Your payment has been processed successfully");
        } catch (Exception ex) {
            // Set response data
            data.put(STATUS, false);
            data.put(MESSAGE, ex.getMessage());
        }
        return ResponseEntity.ok(data);
    }

    /**
     * Retrieve current user payments page
     *
     * @param search: Search criteria
     * @param page:   Requested page
     * @param size:   Request Page Size
     * @return ResponseEntity<Page < Payment>>
     */
    @GetMapping(path = "/")
    public ResponseEntity<Page<Payment>> retrieveUserPaymentsEndpoint(@RequestParam(value = "search", required = false) String search, @RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "${page.default-size}") int size) {

        return ResponseEntity.ok(this.paymentService.getUserPayments(this.authenticationFacade.getAuthentication().getName(), page, size));
    }

    /**
     * Retrieve payment details for the current authenticated user
     *
     * @param id: Payment Identifier
     * @return ResponseEntity<?>
     */
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> retrievePaymentDetails(@PathVariable(value = "id") Long id) {
        // Retrieve payment
        Payment payment = this.paymentService.getPayment(id);
        // Check if payment exists
        if (payment != null) {
            // Check if user is the owner of the payment
            if (payment.getUser().equals(this.userService.getUser(this.authenticationFacade.getAuthentication().getName()))) {
                // Return response
                return ResponseEntity.ok(payment);
            }
            // Return unauthorized response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to access this payment");
        }
        // Return no payment found
        return ResponseEntity.notFound().build();
    }

    /**
     * Download payment details file
     * @param id: Payment Identifier
     * @return ResponseEntity<?>
     */
    @GetMapping(path = "/download/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<?> downloadPaymentDetailsFile(@PathVariable(value = "id") Long id) throws IOException {
        // Retrieve payment
        Payment payment = this.paymentService.getPayment(id);
        // Check if payment exists
        if (payment != null) {
            // Check if user is the owner of the payment
            if (payment.getUser().equals(this.userService.getUser(this.authenticationFacade.getAuthentication().getName()))) {
                // Download file
                return ResponseEntity.ok(this.paymentService.getPaymentFile(id));
            }
            // Return unauthorized response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to download the payment file!");
        }
        // Return no payment found
        return ResponseEntity.notFound().build();
    }

}
