package org.restaurant.salado.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.restaurant.salado.entities.Currency;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Haytham DAHRI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChargeRequest implements Serializable {

    private String description;
    private BigDecimal amount;
    private Currency currency;
    private String stripeEmail;
    private String stripeToken;

}
