package org.restaurant.salado.repositories;

import org.restaurant.salado.entities.Role;
import org.restaurant.salado.entities.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
@RepositoryRestResource
@CrossOrigin(value = "*")
public interface RoleRepository extends JpaRepository<Role, Long> {

    public Role findByRoleName(@Param("roleName") RoleType roleType);

}
