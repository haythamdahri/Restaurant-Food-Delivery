package org.restaurant.salado.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author Haytham DAHRI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotNull
    private String email;
    @NotNull
    private String username;
    @NotNull
    private String location;

}
