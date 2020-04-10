package org.restaurant.salado.services;

import org.restaurant.salado.entities.Review;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author Haytam DAHRI
 */
public interface ReviewService {

    Review saveReview(Review review);

    boolean deleteReview(Long id);

    Review getReview(Long id);

    List<Review> getReviews();

    Page<Review> getReviews(int page, int size);

    Page<Review> getMealReviews(Long mealId, int page, int size);

    List<Review> getUserReviews(String email);

}
