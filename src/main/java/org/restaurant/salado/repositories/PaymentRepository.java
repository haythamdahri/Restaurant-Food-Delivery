package org.restaurant.salado.repositories;

import org.restaurant.salado.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * @author Haytham DAHRI
 */
@Repository
@RepositoryRestResource
@CrossOrigin(value = "*")
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
