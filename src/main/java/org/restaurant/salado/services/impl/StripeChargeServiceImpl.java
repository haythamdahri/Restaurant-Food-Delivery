package org.restaurant.salado.services.impl;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.restaurant.salado.entities.Currency;
import org.restaurant.salado.entities.User;
import org.restaurant.salado.models.ChargeRequest;
import org.restaurant.salado.services.ChargeService;
import org.restaurant.salado.services.PostPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Haytham DAHRI
 */
@Service
public class StripeChargeServiceImpl implements ChargeService {

    @Value("${STRIPE_SECRET_KEY}")
    private static String secretKey;

    private PostPaymentService postPaymentService;

    @PostConstruct
    public static void init() {
        Stripe.apiKey = secretKey;
    }

    @Autowired
    public void setPostPaymentService(PostPaymentService postPaymentService) {
        this.postPaymentService = postPaymentService;
    }

    @Override
    public Charge chargeCreditCard(ChargeRequest chargeRequest, User user) throws StripeException {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", chargeRequest.getAmount());
        chargeParams.put("currency", chargeRequest.getCurrency());
        chargeParams.put("description", chargeRequest.getDescription());
        chargeParams.put("source", chargeRequest.getStripeToken());
        Charge charge = Charge.create(chargeParams);
        // Run Async post payment
        this.postPaymentService.postCharge(charge.getId(), user);
        // Return charge
        return charge;
    }

    @Override
    @Async
    public void chargeCreditCard(String token, BigDecimal amount, User user) throws StripeException {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", amount.intValue() * 100);
        chargeParams.put("currency", Currency.MAD);
        chargeParams.put("source", token);
        Charge charge = Charge.create(chargeParams);
        // Run Async post payment
        this.postPaymentService.postCharge(charge.getId(), user);
    }
}
