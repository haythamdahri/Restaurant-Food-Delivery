package org.restaurant.salado.facades;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author Haytham DAHRI
 */
@Component
public class AuthenticationFacade implements IAuthenticationFacede {

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
