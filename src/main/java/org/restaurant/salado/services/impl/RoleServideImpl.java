package org.restaurant.salado.services.impl;

import org.restaurant.salado.entities.Role;
import org.restaurant.salado.entities.RoleType;
import org.restaurant.salado.repositories.RoleRepository;
import org.restaurant.salado.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Haytam DAHRI
 */
@Service
public class RoleServideImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role saveRole(Role role) {
        return this.roleRepository.save(role);
    }

    @Override
    public Role getRole(Long id) {
        return this.roleRepository.findById(id).orElse(null);
    }

    @Override
    public Role getRole(RoleType roleType) {
        return this.roleRepository.findByRoleName(roleType);
    }

    @Override
    public boolean deleteRole(Long id) {
        this.roleRepository.deleteById(id);
        return true;
    }

    @Override
    public List<Role> getRoles() {
        return this.roleRepository.findAll();
    }
}
