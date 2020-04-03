package org.restaurant.salado.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

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

    @Column(name = "total_price")
    private double totalPrice;

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

    // Convenient method to add a new meal to the current order
    public void addMeal(MealOrder mealOrder) {
        if (this.mealOrders == null) {
            this.mealOrders = new ArrayList<>();
        }
        this.mealOrders.add(mealOrder);
    }

    // Callculate the new total ice
    public void calculateTotalPrice() {
        this.totalPrice = 0;
        if (this.mealOrders != null) {
            for (MealOrder mealOrder : this.mealOrders) {
                this.totalPrice += mealOrder.getPrice() * mealOrder.getQuantity();
            }
        }
    }

}
