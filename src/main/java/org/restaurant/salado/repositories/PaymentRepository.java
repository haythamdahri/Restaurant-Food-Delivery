package org.restaurant.salado.repositories;

import org.restaurant.salado.entities.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.math.BigDecimal;

/**
 * @author Haytham DAHRI
 */
@Repository
@RepositoryRestResource
@CrossOrigin(value = "*")
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Page<Payment> findByUserEmail(@PageableDefault Pageable pageable, @Param(value = "email") String email);

    @Query("SELECT p from Payment as p where p.id = :search or p.order.id = :search or p.user.id = :search or p.order.price = (:search - :shippingFees)")
    Page<Payment> searchPaymentsByIdentifier(@PageableDefault Pageable pageable, @Param("search") Long search, @Param("shippingFees") BigDecimal shippingFees);

    @Query("SELECT p from Payment as p where p.chargeId = :search  or lower(p.user.email) like %:search% or lower(p.user.username) like %:search% or lower(p.user.location) like %:search%")
    Page<Payment> searchPayments(@PageableDefault Pageable pageable, @Param("search") String search);

}
