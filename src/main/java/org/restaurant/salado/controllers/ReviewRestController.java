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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

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
     * Retrieve all reviews
     * Allow only Employees Or Admins to access this resources
     *
     * @return ResponseEntity<List < Review>>
     */
    @PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/all")
    public ResponseEntity<List<Review>> retrieveAllReviews() {
        return ResponseEntity.ok(this.reviewService.getReviews());
    }

    /**
     * Get a given meal reviews Endpoint
     *
     * @param mealId: Meal Identifier
     * @param page:   Requested Page
     * @param size:   Requested Page Size
     * @return ResponseEntity<Page < Review>>
     */
    @PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/")
    public ResponseEntity<Page<Review>> retrieveReviewsEndPoint(@RequestParam(value = "search", required = false) String search, @RequestParam(value = "meal", required = false) Long mealId, @RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "${page.default-size}") int size) {
        // Check if search
        if (search != null) {
            return ResponseEntity.ok(this.reviewService.getReviews(search, page, size));
        } else if (mealId != null) {
            return ResponseEntity.ok(this.reviewService.getMealReviews(mealId, page, size));
        }
        // Return paged reviews if no meal and search is requested
        return ResponseEntity.ok(this.reviewService.getReviews(page, size));
    }


    /**
     * Approve Review
     * Authorize only Employees And Admins
     *
     * @param reviewId: Review Identifier
     * @return ResponseEntity<Review>
     */
    @PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/approve")
    public ResponseEntity<Review> approveReview(@RequestParam(value = "id") Long reviewId) {
        try {
            // Enable account
            return ResponseEntity.ok(this.reviewService.approveReview(reviewId));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Disapprove Review
     * Authorize only Employees And Admins
     *
     * @param reviewId: Review Identifier
     * @return ResponseEntity<Review>
     */
    @PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/disapprove")
    public ResponseEntity<Review> disapproveReview(@RequestParam(value = "id") Long reviewId) {
        try {
            // Enable account
            return ResponseEntity.ok(this.reviewService.disapproveReview(reviewId));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get a given meal approved reviews Endpoint
     * Retrieve only approved ones
     *
     * @param mealId: Meal Identifier
     * @param page:   Requested Page
     * @param size:   Requested Page Size
     * @return ResponseEntity<Page < Review>>
     */
    @GetMapping(value = "/approved")
    public ResponseEntity<Page<Review>> retrieveApprovedReviewsEndPoint(@RequestParam(value = "meal", required = false) Long mealId, @RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "${page.default-size}") int size) {
        // Check if meal Id
        if (mealId != null) {
            return ResponseEntity.ok(this.reviewService.getApprovedMealReviews(mealId, page, size));
        }
        // Return paged reviews if no meal is requested
        return ResponseEntity.ok(this.reviewService.getApprovedReviews(page, size));
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
        Review review = new Review(null, user, meal, reviewRequest.getComment(), false, reviewRequest.getRating(), null);
        // Save Review
        review = this.reviewService.saveReview(review);
        // Return success response
        return ResponseEntity.ok(review);
    }


    /**
     * Retrieve Meal object of a given Review
     *
     * @param id: Review IDENTIFIER
     * @return Meal
     */
    @GetMapping(path = "/{id}/meal")
    @Transactional
    public ResponseEntity<Meal> retrieveReviewMeal(@PathVariable(value = "id") Long id) {
        try {
            return ResponseEntity.ok(this.reviewService.getReview(id).getMeal());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
