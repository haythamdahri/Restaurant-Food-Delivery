package org.restaurant.salado.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author Haytham DAHRI
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserId implements Serializable {

    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

}
