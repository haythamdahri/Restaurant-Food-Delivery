package org.restaurant.salado.services.impl;

import org.restaurant.salado.entities.User;
import org.restaurant.salado.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Haytam DAHRI
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        // Fetch user from database using his email
        User user = this.userService.getEnabledUser(email);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        if (user != null) {
            user.getRoles().forEach(role ->
                    grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName().name()))
            );
            return new org.springframework.security.core.userdetails.User(user.getUserId().getEmail(), user.getPassword(), grantedAuthorities);
        }
        throw new UsernameNotFoundException("No user found with " + email);
    }
}
