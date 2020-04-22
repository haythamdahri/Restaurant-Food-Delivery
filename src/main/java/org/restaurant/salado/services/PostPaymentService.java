package org.restaurant.salado.services;

import org.restaurant.salado.entities.Payment;
import org.restaurant.salado.dtos.ShippingDTO;

import java.util.concurrent.CompletableFuture;

/**
 * @author Haytham DAHRI
 */
public interface PostPaymentService {

    /**
     * Post charge business logic service
     * Empty user cart
     * Send payment notification
     * Asynchronous operation
     *
     * @param chargeId:    Done charge Id
     * @param email:       User email who done Charge
     * @param shippingDTO: ShippingDTO object
     * @return CompletableFuture<Payment>
     */
    CompletableFuture<Payment> postCharge(String chargeId, String email, ShippingDTO shippingDTO);

}
