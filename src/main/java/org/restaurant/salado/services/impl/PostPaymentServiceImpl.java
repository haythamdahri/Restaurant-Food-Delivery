package org.restaurant.salado.services.impl;

import org.restaurant.salado.entities.Order;
import org.restaurant.salado.entities.Payment;
import org.restaurant.salado.entities.User;
import org.restaurant.salado.facades.AuthenticationFacade;
import org.restaurant.salado.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.concurrent.CompletableFuture;

/**
 * @author Haytham DAHRI
 */
@Service
public class PostPaymentServiceImpl implements PostPaymentService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    /**
     * Run post payment actions
     * Modify products stock
     * Invoke post payment service
     * Send email to user about successful purchase
     *
     * @return Boolean
     * TODO: COMPLETE METHOD
     */
    @Override
    @Transactional
    @Async
    public CompletableFuture<Boolean> postCharge(String chargeId, User user) throws Exception {
        /**
         * Modify products stock of last active order and save order
         * Set order as delivered
         */
        Order order = this.orderService.getLastActiveOrder(user.getId());
        order.postCharge();
        order.setDelivered(true);
        order.setCancelled(false);
        order = this.orderService.saveOrder(order);
        // Create payment
        Payment payment = new Payment(null, user, chargeId, null, null);
        payment = this.paymentService.savePayment(payment);
        // Send Post Payment Email
        this.emailService.sendPostPaymentEmail(user.getEmail(), "Payment Notification", payment.getId(), payment.getTimestamp());
        // Return
        return CompletableFuture.completedFuture(true);
    }

}
