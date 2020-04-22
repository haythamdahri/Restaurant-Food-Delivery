package org.restaurant.salado.services.impl;

import org.restaurant.salado.entities.Order;
import org.restaurant.salado.entities.Payment;
import org.restaurant.salado.entities.Shipping;
import org.restaurant.salado.dtos.ShippingDTO;
import org.restaurant.salado.mappers.ShippingMapper;
import org.restaurant.salado.services.EmailService;
import org.restaurant.salado.services.OrderService;
import org.restaurant.salado.services.PaymentService;
import org.restaurant.salado.services.PostPaymentService;
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

    private OrderService orderService;

    private EmailService emailService;

    private PaymentService paymentService;

    private ShippingMapper shippingMapper;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Autowired
    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Autowired
    public void setShippingMapper(ShippingMapper shippingMapper) {
        this.shippingMapper = shippingMapper;
    }

    /**
     * Run post payment actions
     * Modify products stock
     * Invoke post payment service
     * Send email to user about successful purchase
     *
     * @return CompletableFuture<Payment>
     */
    @Override
    @Transactional
    @Async
    public CompletableFuture<Payment> postCharge(String chargeId, String email, ShippingDTO shippingDTO) {
        // Map ShippingDTO to Shipping
        Shipping shipping = this.shippingMapper.toModel(shippingDTO);
        // Modify products stock of last active order and save order
        // Set order as delivered
        // Set shipping details
        Order order = this.orderService.getLastActiveOrder(email);
        // Assert order not null
        assert order != null;
        order.postCharge();
        order.setDelivered(true);
        order.setCancelled(false);
        order.setShipping(shipping);
        order = this.orderService.saveOrder(order);
        // Create payment
        Payment payment = new Payment(null, order.getUser(), order, chargeId, null, null);
        payment = this.paymentService.savePayment(payment);
        // Send Post Payment Email
        this.emailService.sendPostPaymentEmail(email, "Payment Notification", payment.getId(), payment.getTimestamp());
        // Return
        return CompletableFuture.completedFuture(payment);
    }

}
