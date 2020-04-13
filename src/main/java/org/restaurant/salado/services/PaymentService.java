package org.restaurant.salado.services;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.restaurant.salado.entities.Currency;
import org.restaurant.salado.models.ChargeRequest;

/**
 * @author Haytham DAHRI
 */
public interface PaymentService {

    Charge charge(ChargeRequest chargeRequest) throws Exception;

}
