package org.restaurant.salado.services;

import com.stripe.model.Charge;
import org.restaurant.salado.entities.User;
import org.restaurant.salado.models.ChargeRequest;

import java.math.BigDecimal;

/**
 * @author Haytham DAHRI
 */
public interface ChargeService {

    Charge chargeCreditCard(ChargeRequest chargeRequest, User user) throws Exception;

    Charge chargeCreditCard(String token, BigDecimal amount, User user) throws Exception;

}
