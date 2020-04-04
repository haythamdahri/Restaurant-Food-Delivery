package org.restaurant.salado.models;

import java.io.Serializable;

/**
 * @author Haytam DAHRI
 */
public class JwtResponse implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;

    /**
     * @param jwttoken
     */
    public JwtResponse(String jwttoken) {
        this.jwttoken = jwttoken;
    }

    /**
     * @return
     */
    public String getToken() {
        return this.jwttoken;
    }
}
