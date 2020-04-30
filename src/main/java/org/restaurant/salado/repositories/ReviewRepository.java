package org.restaurant.salado.repositories;

import org.restaurant.salado.entities.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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


    Page<Review> findByApprovedTrue(@PageableDefault Pageable pageable);

    Page<Review> findByMealIdOrderById(@PageableDefault Pageable pageable, Long mealId);

    Page<Review> findByMealIdAndApprovedTrueOrderById(@PageableDefault Pageable pageable, Long mealId);

    @Query("SELECT r from Review as r where CONCAT(r.id, '') like %:search% or CONCAT(r.approved, '') like %:search% or r.comment like %:search% or r.user.email like %:search% or r.user.username like %:search% " +
            "or r.user.location like %:search%")
    Page<Review> searchReviews(@PageableDefault Pageable pageable, @Param(value = "search") String search);

}
