package org.restaurant.salado.services.impl;

import org.restaurant.salado.entities.Payment;
import org.restaurant.salado.repositories.PaymentRepository;
import org.restaurant.salado.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    private PaymentRepository paymentRepository;

    @Autowired
    public void setPaymentRepository(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment savePayment(Payment payment) {
        return this.paymentRepository.save(payment);
    }

    @Override
    public Boolean deletePayment(Long id) {
        this.paymentRepository.deleteById(id);
        return true;
    }

    @Override
    public Payment getPayment(Long id) {
        return this.paymentRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Payment> getUserPayments(String userEmail, int page, int size) {
        return this.paymentRepository.findByUserEmail(PageRequest.of(page, size), userEmail);
    }

    @Override
    public Page<Payment> getUserPayments(String userEmail, String search, int page, int size) {
        return this.paymentRepository.findByUserEmail(PageRequest.of(page, size), userEmail);
    }

    @Override
    public List<Payment> getPayments() {
        return this.paymentRepository.findAll();
    }
}
