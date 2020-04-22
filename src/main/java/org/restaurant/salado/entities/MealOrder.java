package org.restaurant.salado.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Haytam DAHRI
 */
@Entity
@Table(name = "meal_orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MealOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    @ManyToOne
    @JoinColumn(name = "meal_id")
    private Meal meal;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "total_price")
    private BigDecimal totalPrice = BigDecimal.ZERO;

    public void setOrder(Order order) {
        this.order = order;
        order.addMeal(this);
    }

    /**
     * Convenient method to delete mealOrder from the current order
     */
    public void deleteMealOrderFromOrder(MealOrder mealOrder) {
        this.order.getMealOrders().remove(mealOrder);
    }

    /**
     * Calculate total price method
     * Before Persisting operations
     */
    @PrePersist
    @PreUpdate
    @PreRemove
    private void prePersist() {
        this.totalPrice = BigDecimal.valueOf(this.quantity).multiply(this.meal.getPrice());
        this.order.calculatePrice();
    }

    /**
     * Down quantity
     */
    public void postChargeQuantity() {
        this.meal.reduceQuantity(this.quantity);
    }

}
