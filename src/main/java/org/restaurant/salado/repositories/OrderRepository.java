package org.restaurant.salado.repositories;

import org.restaurant.salado.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

/**
 * @author Haytam DAHRI
 */
@Repository
@RepositoryRestResource
@CrossOrigin(value = "*")
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(@Param("id") Long id);

    List<Order> findByUserEmail(@Param("email") String email);

    /**
     * Fetch last active order for a given user id
     *
     * @param userId
     * @return Order
     */
    Order findByUserIdAndCancelledFalseAndDeliveredFalse(@Param("userId") Long userId);

}
