package org.restaurant.salado.repositories;

import org.restaurant.salado.entities.RoleType;
import org.restaurant.salado.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

/**
 * @author Haytam DAHRI
 */
@Repository
@RepositoryRestResource
@CrossOrigin(value = "*")
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(@Param("email") String email);

    @RestResource(path = "existsByEmail", rel = "checkUserExisting")
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u where CONCAT(u.id, '') <> :criteria and u.email <> :criteria order by u.online desc")
    List<User> searchUserWithExclusion(@Param("criteria") String criteria);

    Optional<User> findByEmailAndEnabledIsTrue(@Param("email") String email);

    Optional<User> findByToken(@Param("token") String token);

    @Query(value = "select u from User u inner join u.roles r where r.roleName in :roleNames and size(u.roles) = 1")
    List<User> findBySpecificRoles(@Param("roleNames") List<RoleType> roleName);

    @Query(value = "select u from User u inner join u.roles r where r.roleName in :roleNames and size(u.roles) = 1 and " +
            "(CONCAT(u.id, '') like %:search% or lower(u.username) like %:search% or lower(u.email) like %:search% or lower(u.location) like %:search%)")
    List<User> findBySpecificRolesAndSearch(@Param("roleNames") List<RoleType> roleName, @Param(value = "search") String search);

    @Query(value = "select u from User u inner join u.roles r where r.roleName in :roleNames and size(u.roles) = 1")
    Page<User> findBySpecificRolesPage(@PageableDefault Pageable pageable, @Param("roleNames") List<RoleType> roleName);

    @Query(value = "select u from User u inner join u.roles r where r.roleName in :roleNames and size(u.roles) = 1 and " +
            "(CONCAT(u.id, '') like %:search% or lower(u.username) like %:search% or lower(u.email) like %:search% or lower(u.location) like %:search%)")
    Page<User> findBySpecificRolesAndSearchPage(@PageableDefault Pageable pageable, @Param("roleNames") List<RoleType> roleName, @Param(value = "search") String search);

}
