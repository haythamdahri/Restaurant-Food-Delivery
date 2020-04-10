package org.restaurant.salado.controllers;

import org.restaurant.salado.entities.Meal;
import org.restaurant.salado.entities.Review;
import org.restaurant.salado.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/reviews")
public class ReviewRestController {

    @Autowired
    private ReviewService reviewService;

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
}
