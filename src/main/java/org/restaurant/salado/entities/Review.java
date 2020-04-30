package org.restaurant.salado.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Haytham DAHRI
 */
@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumns(value = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    private User user;

    @ManyToOne
    @JoinColumn(name = "meal_id")
    @JsonIgnore
    private Meal meal;

    @Column(name = "comment", length = 1200)
    private String comment;

    @Column(name = "approved")
    private boolean approved;

    @Column(name = "rating")
    @Min(value = 0)
    @Max(value = 5)
    private int rating;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp")
    private Date timestamp;

    @PrePersist
    void createdAt() {
        this.timestamp = new Date();
    }


}
