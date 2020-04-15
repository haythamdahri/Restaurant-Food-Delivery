package org.restaurant.salado.services;

import com.stripe.model.Charge;
import org.restaurant.salado.entities.User;

import java.util.concurrent.CompletableFuture;

/**
 * @author Haytham DAHRI
 */
public interface PostPaymentService {

    CompletableFuture<Boolean> postCharge(String chargeId, User user) throws Exception;

}
