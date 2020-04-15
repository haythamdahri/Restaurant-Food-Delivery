package org.restaurant.salado.facades;

import org.springframework.security.core.Authentication;

/**
 * @author Haytham DAHRI
 */
public interface IAuthenticationFacede {

    Authentication getAuthentication();

}
