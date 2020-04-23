package org.restaurant.salado.services;

import org.restaurant.salado.entities.Payment;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
public interface PaymentService {

    Payment savePayment(Payment payment);

    Boolean deletePayment(Long id);

    Payment getPayment(Long id);

    Page<Payment> getUserPayments(String userEmail, int page, int size);

    Page<Payment> getUserPayments(String userEmail, String search, int page, int size);

    List<Payment> getPayments();

}
