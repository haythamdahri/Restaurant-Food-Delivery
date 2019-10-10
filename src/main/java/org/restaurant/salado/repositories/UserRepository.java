package org.restaurant.salado.repositories;

import org.restaurant.salado.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@Repository
@RepositoryRestResource
@CrossOrigin(value = "*")
public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByEmail(@Param("email") String email);

    public boolean existsByEmail(@Param("email") String email);

    public Optional<User> findByEmailAndEnabledIsTrue(@Param("email") String email);

    public Optional<User> findByToken(@Param("token") String token);

}
