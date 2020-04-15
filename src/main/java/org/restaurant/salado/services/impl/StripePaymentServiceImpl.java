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
import java.util.concurrent.CompletableFuture;

/**
 * @author Haytham DAHRI
 */
@Service
public class StripePaymentServiceImpl implements ChargeService {

    @Value("${STRIPE_SECRET_KEY}")
    private String secretKey;

    @Autowired
    private PostPaymentService postPaymentService;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    @Override
    public Charge chargeCreditCard(ChargeRequest chargeRequest, User user)
            throws Exception {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", chargeRequest.getAmount());
        chargeParams.put("currency", chargeRequest.getCurrency());
        chargeParams.put("description", chargeRequest.getDescription());
        chargeParams.put("source", chargeRequest.getStripeToken());
        Charge charge = Charge.create(chargeParams);
        // Run post payment with other Thread
        this.postPaymentService.postCharge(charge.getId(), user);
        // Return charge
        return charge;
    }

    @Override
    @Async
    public Charge chargeCreditCard(String token, BigDecimal amount, User user)
            throws StripeException, Exception {
        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("amount", amount.intValue() * 100);
        chargeParams.put("currency", Currency.MAD);
        chargeParams.put("source", token);
        Charge charge = Charge.create(chargeParams);
        // Run post payment with other Thread
        CompletableFuture<Boolean> done = this.postPaymentService.postCharge(charge.getId(), user);
        System.out.println("Done: " + done.get());
        // Return charge
        return charge;
    }
}
