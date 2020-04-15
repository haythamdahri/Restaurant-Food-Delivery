package org.restaurant.salado.services;

import org.restaurant.salado.entities.Payment;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
public interface PaymentService {

    Payment savePayment(Payment payment);

    Boolean deletePayment(Long id);

    Payment getPayment(Long id);

    List<Payment> getPayments();

}
