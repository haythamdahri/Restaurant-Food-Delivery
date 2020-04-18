package org.restaurant.salado.models;

import lombok.*;

import java.io.Serializable;

/**
 * @author Haytham DAHRI
 */
@Data
public class ResponseData implements Serializable {

    private boolean status;
    private String message;
    private Object extra;

    private static ResponseData responseData;

    public ResponseData() {

    }

    public ResponseData(String message, boolean status, Object extra) {
        this.message = message;
        this.status = status;
        this.extra = extra;
    }

    private ResponseData(String message, boolean status) {
        this.message = message;
        this.status = status;
    }

    public static ResponseData getInstance(String message, boolean status) {
        if( responseData == null ) {
            responseData = new ResponseData(message, status);
        } else {
            message = message;
            status = status;
        }
        return responseData;
    }

    public static ResponseData getInstance(String message, boolean status, Object extra) {
        if( responseData == null ) {
            responseData = new ResponseData(message, status, extra);
        } else {
            message = message;
            status = status;
            extra = extra;
        }
        return responseData;
    }

}
