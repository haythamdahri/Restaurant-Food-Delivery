package org.restaurant.salado;

import io.jsonwebtoken.lang.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.restaurant.salado.entities.Payment;
import org.restaurant.salado.repositories.PaymentRepository;
import org.restaurant.salado.services.impl.PaymentServiceImpl;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Before
    public void setUp() {
    }

    @DisplayName("Test Unique Charge Id For All Payments")
    @Test
    public void retrieveAllPayments_AndExpectUniqueChargeIdForEachRow() {
        List<Payment> payments = this.paymentService.getPayments();
        assert payments != null;
        // Check charge ID is present for all payments if there is at least one payment in Database
        if (!payments.isEmpty()) {
            Assert.isTrue(payments.stream().anyMatch(payment -> payment.getCharge() != null));
            // Check that payments charge ID is unique for each row
            Assert.isTrue(payments.stream().map(Payment::getCharge).distinct().count() == payments.size());
        }
    }

}
