package org.restaurant.salado.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

/**
 * @author Haytham DAHRI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealRequest {

    private Long id;
    private String name;
    private Long stock;
    private Long views;
    private BigDecimal price;
    private BigDecimal salePrice;
    private MultipartFile image;
    private boolean updateImage;

}
