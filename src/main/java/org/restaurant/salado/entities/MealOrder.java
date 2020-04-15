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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    @ManyToOne
    @JoinColumn(name = "meal_id")
    private Meal meal;

    @Column(name = "quantity")
    private int quantity;

    @Transient
    private BigDecimal totalPrice;

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
     */
    @PostLoad
    public void postLoad() {
        this.totalPrice = BigDecimal.valueOf(this.quantity).multiply(this.meal.getPrice());
    }

    /**
     * Before Persisting operations
     */
    @PrePersist
    private void prePersist() {
        BigDecimal totalPrice = BigDecimal.valueOf(this.getQuantity()).multiply(this.getMeal().getPrice());
    }

    /**
     * Down quantity
     */
    public void postChargeQuantity() {
        this.meal.reduceQuantity(this.quantity);
    }

}
