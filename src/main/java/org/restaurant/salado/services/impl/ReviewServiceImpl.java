package org.restaurant.salado.services.impl;

import org.restaurant.salado.entities.Review;
import org.restaurant.salado.exceptions.BusinessException;
import org.restaurant.salado.repositories.ReviewRepository;
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

    private ReviewRepository reviewRepository;

    @Autowired
    public void setReviewRepository(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Review saveReview(Review review) {
        return this.reviewRepository.save(review);
    }

    @Override
    public Review approveReview(Long id) {
        // Retrieve Review
        Review review = this.reviewRepository.findById(id).orElseThrow(BusinessException::new);
        // Update review status
        review.setApproved(true);
        return this.reviewRepository.save(review);
    }

    @Override
    public Review disapproveReview(Long id) {
        // Retrieve Review
        Review review = this.reviewRepository.findById(id).orElseThrow(BusinessException::new);
        // Update review status
        review.setApproved(false);
        return this.reviewRepository.save(review);
    }

    @Override
    public boolean deleteReview(Long id) {
        this.reviewRepository.deleteById(id);
        return true;
    }

    @Override
    public Review getReview(Long id) {
        return this.reviewRepository.findById(id).orElse(null);
    }

    @Override
    public List<Review> getReviews() {
        return this.reviewRepository.findAll();
    }

    @Override
    public Page<Review> getReviews(int page, int size) {
        return this.reviewRepository.findAll(PageRequest.of(page, size, Sort.Direction.DESC, "id"));
    }

    @Override
    public Page<Review> getReviews(String search, int page, int size) {
        return this.reviewRepository.searchReviews(PageRequest.of(page, size, Sort.Direction.DESC, "id"), search.trim().toLowerCase());
    }

    @Override
    public Page<Review> getApprovedReviews(int page, int size) {
        return this.reviewRepository.findByApprovedTrue(PageRequest.of(page, size, Sort.Direction.DESC, "rating"));
    }

    @Override
    public Page<Review> getMealReviews(Long mealId, int page, int size) {
        return this.reviewRepository.findByMealIdOrderById(PageRequest.of(page, size, Sort.Direction.DESC, "rating"), mealId);
    }

    @Override
    public Page<Review> getApprovedMealReviews(Long mealId, int page, int size) {
        return this.reviewRepository.findByMealIdAndApprovedTrueOrderById(PageRequest.of(page, size, Sort.Direction.DESC, "rating"), mealId);
    }

    @Override
    public List<Review> getUserReviews(String email) {
        return this.reviewRepository.findAll();
    }
}
