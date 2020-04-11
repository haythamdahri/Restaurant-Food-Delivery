package org.restaurant.salado.repositories;

import org.restaurant.salado.entities.RestaurantFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @author Haytham DAHRI
 */
@Repository
@RepositoryRestResource
@CrossOrigin(value = "*")
@Transactional
public interface RestaurantFileRepository extends JpaRepository<RestaurantFile, Long> {

    Page<RestaurantFile> findByOrderByIdDesc(@PageableDefault Pageable pageable);

    Optional<RestaurantFile> findByNameContainingIgnoreCase(String name);

    Page<RestaurantFile> findByNameContainingIgnoreCaseOrId(@PageableDefault Pageable pageable, String name, String id);

}
