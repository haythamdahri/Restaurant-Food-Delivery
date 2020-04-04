package org.restaurant.salado.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

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
     * Calculate shipping fees
     */
    void calculateShippingFees() {
        this.totalPrice = this.totalPrice.add(this.shippingFees);
    }

    /**
     * Post Load calculations
     */
    @PostLoad
    public void postLoad() {
        System.out.println("============ CALLING POST LOAD METHOD ORDER ============");
        if (this.mealOrders != null) {
            this.mealOrders.stream().peek(mealOrder -> this.totalPrice = this.totalPrice.add(mealOrder.getTotalPrice()));
        }
        System.out.println("============ Total price order: " + this.totalPrice);
    }

    @PostConstruct
    public void postConstruct() {
        this.postLoad();
    }

}
