package org.restaurant.salado.repositories;

import org.restaurant.salado.entities.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * @author Haytham DAHRI
 */
@Repository
@RepositoryRestResource
@CrossOrigin(value = "*")
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findAllByOrderById(@PageableDefault Pageable pageable);

    Page<Review> findByMealIdOrderById(@PageableDefault Pageable pageable, Long mealId);

}
