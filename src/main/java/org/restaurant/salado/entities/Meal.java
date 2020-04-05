package org.restaurant.salado.entities;

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
@Table(name = "meals")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Meal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true, insertable = true, updatable = true)
    private String name;

    @Column(name = "image")
    private String image;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "stock")
    private Long stock;

    @Column(name = "views")
    private Long views = 0L;

    @Column(name = "sale_price")
    private BigDecimal salePrice;

    /**
     * Retrieve meal price
     * Check if product in sale
     * @return BigDecimal
     */
    public BigDecimal getPrice() {
        return this.salePrice != null ? this.salePrice : this.price;
    }

    /**
     * Increment views for current meal
     */
    public Meal incrementViews() {
        this.views += 1L;
        return this;
    }

}
