package org.restaurant.salado.services;

import org.restaurant.salado.entities.Review;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author Haytam DAHRI
 */
public interface ReviewService {

    Review saveReview(Review review);

    Review approveReview(Long id);

    Review disapproveReview(Long id);

    boolean deleteReview(Long id);

    Review getReview(Long id);

    List<Review> getReviews();

    Page<Review> getReviews(int page, int size);

    Page<Review> getReviews(String search, int page, int size);

    Page<Review> getApprovedReviews(int page, int size);

    Page<Review> getMealReviews(Long mealId, int page, int size);

    Page<Review> getApprovedMealReviews(Long mealId, int page, int size);

    List<Review> getUserReviews(String email);

}
