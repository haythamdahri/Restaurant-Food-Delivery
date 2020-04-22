package org.restaurant.salado.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Haytham DAHRI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingDTO {

    private Long id;
    private String country;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private int postalCode;
    private String email;
    private String phone;

}
