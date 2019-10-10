package org.restaurant.salado.services;

import org.restaurant.salado.entities.Role;
import org.restaurant.salado.entities.RoleType;
import org.restaurant.salado.entities.User;

import java.util.Collection;

public interface RoleService {

    public Role saveRole(Role role);

    public Role getRole(Long id);

    public Role getRole(RoleType roleType);

    public boolean deleteRole(Long id);

    public Collection<Role> getRoles();

}
