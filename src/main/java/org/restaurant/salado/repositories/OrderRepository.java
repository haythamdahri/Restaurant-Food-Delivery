package org.restaurant.salado.repositories;

import org.restaurant.salado.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Collection;

@Repository
@RepositoryRestResource
@CrossOrigin(value = "*")
public interface OrderRepository extends JpaRepository<Order, Long> {

    public Collection<Order> findByUserId(@Param("id") Long id);

    public Collection<Order> findByUserEmail(@Param("email") String email);

    // Fetch last active order
    public Order findByUserIdAndCancelledFalseAndDeliveredFalse(@Param("userId") Long userId);

}
