package org.restaurant.salado.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Haytam DAHRI
 */
@Entity
@Table(name = "shipping")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shipping implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "country", updatable = false, nullable = false)
    private String country;

    @Column(name = "first_name", updatable = false, nullable = false)
    private String firstName;

    @Column(name = "last_name", updatable = false, nullable = false)
    private String lastName;

    @Column(name = "address", updatable = false, nullable = false)
    private String address;

    @Column(name = "city", updatable = false, nullable = false)
    private String city;

    @Column(name = "state", updatable = false, nullable = false)
    private String state;

    @Column(name = "postal_code", updatable = false, nullable = false)
    private int postalCode;

    @Column(name = "email", updatable = false, nullable = false)
    private String email;

    @Column(name = "phone", updatable = false, nullable = false)
    private String phone;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, mappedBy = "shipping")
    @JsonIgnore
    private Order order;

}
