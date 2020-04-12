package org.restaurant.salado.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Haytham DAHRI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest implements Serializable {

    private Long mealId;

    private String comment;

    private int rating;

}
