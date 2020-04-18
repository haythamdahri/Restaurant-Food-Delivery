package org.restaurant.salado.repositories;

import org.restaurant.salado.entities.Meal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

/**
 * @author Haytam DAHRI
 */
@Repository
@RepositoryRestResource
@CrossOrigin(value = "*")
public interface MealRepository extends JpaRepository<Meal, Long> {

    Page<Meal> findTop10ByOrderByViewsDesc(@PageableDefault Pageable pageable);

    List<Meal> findMealByUsersPreferences_UserIdEmail(String email);

}
