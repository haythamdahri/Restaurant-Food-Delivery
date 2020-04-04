package org.restaurant.salado.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Haytam DAHRI
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordReset {

    public String token;

    public String newPassword;

}
