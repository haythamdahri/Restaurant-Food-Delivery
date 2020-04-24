package org.restaurant.salado.services;

import org.restaurant.salado.entities.Payment;
import org.springframework.core.io.InputStreamResource;

import java.io.IOException;

/**
 * @author Haytam DAHRI
 */
public interface PaymentContentBuilder {

    InputStreamResource buildPaymentContent(Payment payment) throws IOException;

}
