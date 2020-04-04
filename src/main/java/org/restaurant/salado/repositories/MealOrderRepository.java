package org.restaurant.salado.repositories;

import org.restaurant.salado.entities.MealOrder;
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
public interface MealOrderRepository extends JpaRepository<MealOrder, Long> {

    List<MealOrder> findByOrderId(@Param("orderId") Long id);

}
