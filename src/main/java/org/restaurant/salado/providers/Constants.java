package org.restaurant.salado.providers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @author Haytham DAHRI
 */
public class Constants {

    public static final BigDecimal SHIPPING_FEES = BigDecimal.valueOf(50.00);
    public static final int DEFAULT_PAGE_SIZE = 5;
    public static final List<String> IMAGE_CONTENT_TYPES = Arrays.asList("image/png", "image/jpeg", "image/gif");

}
