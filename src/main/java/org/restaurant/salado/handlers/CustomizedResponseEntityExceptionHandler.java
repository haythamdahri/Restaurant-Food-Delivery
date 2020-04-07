package org.restaurant.salado.handlers;

import org.restaurant.salado.exceptions.BusinessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author HAYTHAM DAHRI
 * Entity excpetion handler class
 */
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler {

    /**
     * BusinessException handler method
     */
    @ExceptionHandler(value = {BusinessException.class, Exception.class})
    public ResponseEntity<?> handleBusinessExceptions(Exception ex) {
        // Create Payload
        ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse();
        apiExceptionResponse.setMessage(ex.getMessage());
        apiExceptionResponse.setThrowable(ex);
        apiExceptionResponse.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        apiExceptionResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        apiExceptionResponse.setTimestamp(ZonedDateTime.now(ZoneId.of("Z")));
        // return response
        return new ResponseEntity<Object>(apiExceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
