package org.restaurant.salado.services.impl;

import org.restaurant.salado.entities.Meal;
import org.restaurant.salado.entities.Review;
import org.restaurant.salado.repositories.MealRepository;
import org.restaurant.salado.repositories.ReviewRepository;
import org.restaurant.salado.services.MealService;
import org.restaurant.salado.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Haytam DAHRI
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public Review saveReview(Review review) {
        return this.reviewRepository.save(review);
    }

    @Override
    public boolean deleteReview(Long id) {
        this.reviewRepository.deleteById(id);
        return true;
    }

    @Override
    public Review getReview(Long id) {
        return null;
    }

    @Override
    public List<Review> getReviews() {
        return null;
    }

    @Override
    public Page<Review> getReviews(int page, int size) {
        return this.reviewRepository.findAllByOrderById(PageRequest.of(page, size));
    }

    @Override
    public Page<Review> getMealReviews(Long mealId, int page, int size) {
        return this.reviewRepository.findByMealIdOrderById(PageRequest.of(page, size), mealId);
    }

    @Override
    public List<Review> getUserReviews(String email) {
        return this.reviewRepository.findAll();
    }
}
