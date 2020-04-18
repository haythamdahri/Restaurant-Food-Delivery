package org.restaurant.salado.repositories;

import org.restaurant.salado.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

/**
 * @author Haytam DAHRI
 */
@Repository
@RepositoryRestResource
@CrossOrigin(value = "*")
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserIdEmail(@Param("email") String email);

    @RestResource(path = "existsByEmail", rel = "checkUserExisting")
    boolean existsByUserIdEmail(@Param("email") String email);

    Optional<User> findByUserIdEmailAndEnabledIsTrue(@Param("email") String email);

    Optional<User> findByToken(@Param("token") String token);

}
