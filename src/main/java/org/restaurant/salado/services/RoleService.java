package org.restaurant.salado.services;

import org.restaurant.salado.entities.Role;
import org.restaurant.salado.entities.RoleType;

import java.util.List;

public interface RoleService {

    Role saveRole(Role role);

    Role getRole(Long id);

    Role getRole(RoleType roleType);

    boolean deleteRole(Long id);

    List<Role> getRoles();

}
