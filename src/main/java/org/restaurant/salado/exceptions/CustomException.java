package org.restaurant.salado.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Haytham DAHRI
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomException extends Exception implements Serializable {

    private Date timestamp;

    private String message;

    private HttpStatus status;
}
