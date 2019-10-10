package org.restaurant.salado.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.persistence.*;
import java.io.Serializable;

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
    private double price;

    @Column(name = "stock")
    private Long stock;

}
