package org.restaurant.salado.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Haytam DAHRI
 */
@Entity
@Table(name = "meals")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Meal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = RestaurantFile.class)
    @JoinColumn(name = "image_id")
    private RestaurantFile image;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "stock")
    private Long stock;

    @Column(name = "views")
    private Long views = 0L;

    @Column(name = "sale_price")
    private BigDecimal salePrice;

    @Column(name = "deleted")
    private boolean deleted;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "meal", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @JsonIgnore
    @ManyToMany(mappedBy = "preferredMeals", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, targetEntity = User.class)
    private List<User> usersPreferences;

    /**
     * Increment views for current meal
     */
    public Meal incrementViews() {
        this.views += 1L;
        return this;
    }

    /**
     * Convenient method to add a new to the current meal
     *
     * @param review: Review Object to add to current meal reviews list
     */
    public void addReview(Review review) {
        if (this.reviews == null) {
            this.reviews = new ArrayList<>();
        }
        this.reviews.add(review);
    }

    /**
     * Reduce quantity after payment charge
     * @param quantity: current meal stock
     */
    public void reduceQuantity(int quantity) {
        this.stock -= quantity;
    }

}
