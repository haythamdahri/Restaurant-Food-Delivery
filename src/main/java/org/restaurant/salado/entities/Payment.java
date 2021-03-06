package org.restaurant.salado.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stripe.model.Charge;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Haytham DAHRI
 */
@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(targetEntity = User.class)
    @JoinColumns(value = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    private User user;

    @ManyToOne(targetEntity = Order.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Order order;

    @Column(name = "cahrge_id")
    private String chargeId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date timestamp;

    @Transient
    private Charge charge;

    /**
     * Operations to execute before each persist
     */
    @PrePersist
    public void prePersist() {
        this.timestamp = new Date();
    }

}
