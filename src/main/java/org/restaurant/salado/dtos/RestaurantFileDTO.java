package org.restaurant.salado.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Haytham DAHRI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantFileDTO {

    private Long id;

    private String name;

    private String extension;

    private String mediaType;

    private byte[] file;

    private Date timestamp;

}
