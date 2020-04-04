package org.restaurant.salado.handlers;

import org.restaurant.salado.exceptions.CustomException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

/**
 * @author Haytham DAHRI
 */
@ControllerAdvice
public class HttpRestHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<?> handleGenericExceptions(Exception ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        CustomException customException = new CustomException(new Date(), ex.getMessage(), status);
        return new ResponseEntity<CustomException>(customException, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
