package org.restaurant.salado.providers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author Haytham DAHRI
 */
@Configuration
public class ValuesProvider {

    public static final BigDecimal SHIPPING_FEES = BigDecimal.valueOf(50.00);

}