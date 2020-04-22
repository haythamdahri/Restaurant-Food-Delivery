package org.restaurant.salado.services;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.restaurant.salado.dtos.ShippingDTO;
import org.restaurant.salado.models.ChargeRequest;

import java.util.concurrent.CompletableFuture;

/**
 * @author Haytham DAHRI
 */
public interface ChargeService {

    /**
     * Charge user
     * @param chargeRequest: Object contains charge details
     * @param email: User email to charge
     * @param shippingDTO: ShippingDTO object
     * @return  CompletableFuture<Charge>
     * @throws StripeException: Thrown on unhandled payment exception
     */
    CompletableFuture<Charge> chargeCreditCard(ChargeRequest chargeRequest, String email, ShippingDTO shippingDTO) throws StripeException;

    /**
     *
     * @param token : Payment token sent from client application
     * @param email : User email to charge
     * @param shippingDTO : ShippingDTO object
     * @return CompletableFuture<Charge>
     * @throws StripeException: Thrown on unhandled payment exception
     */
    CompletableFuture<Charge> chargeCreditCard(String token, String email, ShippingDTO shippingDTO) throws StripeException;

}
