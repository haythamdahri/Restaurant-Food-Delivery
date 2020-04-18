package org.restaurant.salado.services;

import org.restaurant.salado.entities.Payment;
import org.restaurant.salado.entities.User;

import java.util.concurrent.CompletableFuture;

/**
 * @author Haytham DAHRI
 */
public interface PostPaymentService {

    CompletableFuture<Payment> postCharge(String chargeId, User user);

}
