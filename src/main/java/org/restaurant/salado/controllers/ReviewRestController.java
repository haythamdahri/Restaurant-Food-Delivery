package org.restaurant.salado.controllers;

import org.restaurant.salado.entities.Meal;
import org.restaurant.salado.entities.Review;
import org.restaurant.salado.entities.User;
import org.restaurant.salado.facades.IAuthenticationFacade;
import org.restaurant.salado.models.ReviewRequest;
import org.restaurant.salado.services.MealService;
import org.restaurant.salado.services.ReviewService;
import org.restaurant.salado.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/reviews")
public class ReviewRestController {

    private ReviewService reviewService;

    private UserService userService;

    private MealService mealService;

    private IAuthenticationFacade authenticationFacade;

    @Autowired
    public void setReviewService(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setMealService(MealService mealService) {
        this.mealService = mealService;
    }

    @Autowired
    public void setAuthenticationFacade(IAuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    /**
     * Get all reviews Endpoint
     *
     * @param mealId: Meal Identifier
     * @param page:   Requested Page
     * @param size:   Requested Page Size
     * @return ResponseEntity<Page < Review>>
     */
    @GetMapping(value = "/")
    public ResponseEntity<Page<Review>> retrieveReviewsEndPoint(@RequestParam(value = "meal", required = false) Long mealId, @RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "${page.default-size}") int size) {
        // Check if meal Id
        if (mealId != null) {
            return ResponseEntity.ok(this.reviewService.getMealReviews(mealId, page, size));
        }
        // Return paged reviews if no meal is requested
        return ResponseEntity.ok(this.reviewService.getReviews(page, size));
    }

    /**
     * Add new review for a specific meal
     *
     * @param reviewRequest: ReviewRequest Object sent from client application
     * @return ResponseEntity
     */
    @PostMapping(path = "/")
    public ResponseEntity<Review> addReview(@RequestBody ReviewRequest reviewRequest) {
        // Retrieve Meal
        Meal meal = this.mealService.getMeal(reviewRequest.getMealId());
        // Create a new review object
        User user = this.userService.getUser(this.authenticationFacade.getAuthentication().getName());
        Review review = new Review(null, user, meal, reviewRequest.getComment(), reviewRequest.getRating(), null);
        // Save Review
        review = this.reviewService.saveReview(review);
        // Return success response
        return ResponseEntity.ok(review);
    }
}
