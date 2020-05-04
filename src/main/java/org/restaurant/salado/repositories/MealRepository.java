package org.restaurant.salado.repositories;

import org.restaurant.salado.entities.Meal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
@RepositoryRestResource(exported = false)
@CrossOrigin(value = "*")
public interface MealRepository extends JpaRepository<Meal, Long> {

    Page<Meal> findAllByDeletedFalse(@PageableDefault Pageable pageable);

    Optional<Meal> findByIdAndDeletedFalse(Long id);

    List<Meal> findAllByDeletedFalse();

    Page<Meal> findTop10ByDeletedFalseOrderByViewsDesc(@PageableDefault Pageable pageable);

    @Query(value = "SELECT m from Meal as m where m.deleted = false and (CONCAT(m.id, '') like %:search% or lower(m.name) like %:search% or CONCAT(m.salePrice, '') like %:search% or CONCAT(m.price, '') like %:search% or " +
            "CONCAT(m.views, '') like %:search% or CONCAT(m.stock, '') like %:search%)")
    Page<Meal> searchMeals(@PageableDefault Pageable pageable, String search);

    @Query(value = "SELECT m from Meal as m where CONCAT(m.id, '') like %:search% or lower(m.name) like %:search% or CONCAT(m.salePrice, '') like %:search% or CONCAT(m.price, '') like %:search% or " +
            "CONCAT(m.views, '') like %:search% or CONCAT(m.stock, '') like %:search%")
    Page<Meal> searchAllMeals(@PageableDefault Pageable pageable, String search);

    List<Meal> findMealByUsersPreferences_EmailAndDeletedFalse(String email);

}
