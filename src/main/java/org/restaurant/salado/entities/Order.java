package org.restaurant.salado.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

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
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"roles", "hibernateLazyInitializer"})
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "orders_meal_orders", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "meal_id"))
    private Collection<MealOrder> mealOrders;

    @Transient
    private BigDecimal price;

    @Transient
    private BigDecimal totalPrice;

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

    @OneToOne(fetch = FetchType.EAGER, optional = true, cascade = CascadeType.ALL, targetEntity = Shipping.class, orphanRemoval = true)
    @JoinColumn(name = "shipping_id")
    private Shipping shipping;

    /**
     * Convenient method to add a new meal to the current order
     *
     * @param mealOrder
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
    @PostConstruct
    public void postLoad() {
        if (this.mealOrders != null) {
            this.price = this.mealOrders.stream()
                    .map(MealOrder::getTotalPrice)    // map MealOrder
                    .reduce(BigDecimal.ZERO, BigDecimal::add);      // reduce results
        }
    }

    /**
     * shipping fees setter
     * Calculate total price
     */
    public void setShippingFees(BigDecimal shippingFees) {
        this.shippingFees = shippingFees;
        this.totalPrice = this.price.add(shippingFees);
    }

}
