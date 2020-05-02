package org.restaurant.salado.services.impl;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.restaurant.salado.dtos.ShippingDTO;
import org.restaurant.salado.entities.Currency;
import org.restaurant.salado.entities.Order;
import org.restaurant.salado.exceptions.BusinessException;
import org.restaurant.salado.models.ChargeRequest;
import org.restaurant.salado.providers.Constants;
import org.restaurant.salado.services.ChargeService;
import org.restaurant.salado.services.OrderService;
import org.restaurant.salado.services.PostPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author Haytham DAHRI
 */
@Service
@Transactional
public class StripeChargeServiceImpl implements ChargeService {

    @Value("${STRIPE_SECRET_KEY}")
    private String secretKey;

    private PostPaymentService postPaymentService;

    private OrderService orderService;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    @Autowired
    public void setPostPaymentService(PostPaymentService postPaymentService) {
        this.postPaymentService = postPaymentService;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public CompletableFuture<Charge> chargeCreditCard(ChargeRequest chargeRequest, String email, ShippingDTO shippingDTO) throws StripeException {
        // Retrieve last order
        Order userActiveOrder = this.orderService.getLastActiveOrder(email);
        // Check if there is an order in place
        if (userActiveOrder == null) {
            // Throw exception for no order in progress
            throw new BusinessException(Constants.NO_ORDER_IN_PROGRESS);
        }
        // Proceed to payment
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", chargeRequest.getAmount());
        chargeParams.put("currency", chargeRequest.getCurrency());
        chargeParams.put("description", chargeRequest.getDescription());
        chargeParams.put("source", chargeRequest.getStripeToken());
        Charge charge = Charge.create(chargeParams);
        // Run Async post payment
        this.postPaymentService.postCharge(charge.getId(), email, shippingDTO);
        // Return Charge
        return CompletableFuture.completedFuture(charge);
    }

    @Override
    public CompletableFuture<Charge> chargeCreditCard(String token, String email, ShippingDTO shippingDTO) throws StripeException {
        // Retrieve last order
        Order userActiveOrder = this.orderService.getLastActiveOrder(email);
        // Check if there is an order in place
        if (userActiveOrder == null) {
            // Throw exception for no order in progress
            throw new BusinessException(Constants.NO_ORDER_IN_PROGRESS);
        }
        // Check if products stock is always available
        if (!userActiveOrder.isMealsStockAvailable()) {
            throw new BusinessException(Constants.PRODUCT_STOCK_INSUFFICIENT);
        }
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", userActiveOrder.getTotalPrice().intValue() * 100);
        chargeParams.put("currency", Currency.MAD);
        chargeParams.put("source", token);
        Charge charge = Charge.create(chargeParams);
        // Run Async post payment
        this.postPaymentService.postCharge(charge.getId(), email, shippingDTO);
        // Return Charge
        return CompletableFuture.completedFuture(charge);
    }
}
