package org.restaurant.salado.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.restaurant.salado.providers.Constants;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Haytam DAHRI
 */
@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumns(value = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    @JsonIgnoreProperties({"roles", "hibernateLazyInitializer"})
    private User user;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "orders_meal_orders", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "meal_id"))
    private List<MealOrder> mealOrders;

    @Column(name = "price")
    private BigDecimal price = BigDecimal.ZERO;

    @Transient
    private BigDecimal totalPrice = BigDecimal.ZERO;

    @Transient
    @Value("${shipping.price}")
    private BigDecimal shippingFees;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time")
    private Date time;

    @Column(name = "delivered")
    private boolean delivered;

    @Column(name = "cancelled")
    private boolean cancelled;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = Shipping.class, orphanRemoval = true)
    @JoinColumn(name = "shipping_id")
    private Shipping shipping;

    /**
     * Convenient method to add a new meal to the current order
     *
     * @param mealOrder: MealOrder object
     */
    public void addMeal(MealOrder mealOrder) {
        if (this.mealOrders == null) {
            this.mealOrders = new ArrayList<>();
        }
        this.mealOrders.add(mealOrder);
    }

    /**
     * Post Load calculations
     */
    @PostLoad
    public void postLoad() {
        this.totalPrice = this.shippingFees = BigDecimal.ZERO;
        this.shippingFees = Constants.SHIPPING_FEES;
        this.totalPrice = this.price.add(Constants.SHIPPING_FEES);  // Add Shipping Fees
    }

    /**
     * Calculate price before each persist
     */
    @PrePersist
    @PreUpdate
    public void calculatePrice() {
        if (this.mealOrders != null && !this.mealOrders.isEmpty()) {
            this.price = BigDecimal.ZERO;
            this.price = this.mealOrders.stream()
                    .map(MealOrder::getTotalPrice)    // Map MealOrder
                    .reduce(BigDecimal.ZERO, BigDecimal::add);      // Reduce results
        }
    }

    /**
     * Post charge method to update quantity of each meal after payment
     */
    public void postCharge() {
        if (this.mealOrders != null) {
            this.mealOrders.forEach(MealOrder::postChargeQuantity);
        }
    }

    public boolean checkMealOrder(Long mealOrderId) {
        if (this.mealOrders == null)
            return false;
        return this.mealOrders.stream().anyMatch(ml -> ml.getMeal().getId().equals(mealOrderId));
    }

    public boolean isMealsStockAvailable() {
        for (MealOrder mealOrder : this.mealOrders) {
            if( mealOrder.getQuantity() > mealOrder.getMeal().getStock() ){
                return false;
            }
        }
        return true;
    }

    public boolean isMealsNotDeleted() {
        for( MealOrder mealOrder : this.mealOrders ) {
            if( mealOrder.getMeal().isDeleted() ) {
                return false;
            }
        }
        return true;
    }

}
