package org.restaurant.salado.services;

import org.restaurant.salado.entities.Role;
import org.restaurant.salado.entities.RoleType;
import org.restaurant.salado.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

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
        Optional<Role> optionalRole = this.roleRepository.findById(id);
        return optionalRole.orElse(null);
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
    public Collection<Role> getRoles() {
        return this.roleRepository.findAll();
    }
}
