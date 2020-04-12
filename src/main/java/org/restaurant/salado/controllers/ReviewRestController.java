package org.restaurant.salado.controllers;

import org.restaurant.salado.entities.Meal;
import org.restaurant.salado.entities.Review;
import org.restaurant.salado.entities.User;
import org.restaurant.salado.models.ReviewRequest;
import org.restaurant.salado.services.MealService;
import org.restaurant.salado.services.ReviewService;
import org.restaurant.salado.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/reviews")
public class ReviewRestController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private MealService mealService;

    /**
     * Retrieve all reviews Endpoint
     *
     * @return ResponseEntity<Page<Review>>
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<Page<Review>> retrieveReviewsEndPoint(@RequestParam(value = "meal", required = false) Long mealId, @RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "${page.default_size}") int size) throws InterruptedException {
        for( int i=0; i<125000; i++ ) {
            System.out.println(i);
        }
        // Check if meal Id
        if( mealId != null ) {
            return ResponseEntity.ok(this.reviewService.getMealReviews(mealId, page, size));
        }
        // Return paged reviews if no meal is requested
        return ResponseEntity.ok(this.reviewService.getReviews(page, size));
    }

    /**
     * Add new review for a specific meal
     * @param reviewRequest
     * @param authentication
     * @return ResponseEntity
     * @throws Exception
     */
    @RequestMapping(path = "/", method = RequestMethod.POST)
    public ResponseEntity<?> addReview(@RequestBody ReviewRequest reviewRequest, @AuthenticationPrincipal Authentication authentication) throws Exception{
        System.out.println("REVIEW ============================> POST");
        System.out.println(reviewRequest);
        // Check if user is authenticated
        if( authentication != null ) {
            // Retrieve current authenticated user
            User user = this.userService.getUser(authentication.getName());
            if( user != null ) {
                // Retrieve Meal
                Meal meal = this.mealService.getMeal(reviewRequest.getMealId());
                // Create a new review object
                Review review = new Review(null, user, meal, reviewRequest.getComment(), reviewRequest.getRating(), null);
                // Save Review
                review = this.reviewService.saveReview(review);
                // Return success response
                return ResponseEntity.ok(review);
            } else {
                // In case of not found user, throw exception
                throw new Exception("No authenticated user found");
            }
        }
        // Return unauthorized response
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
