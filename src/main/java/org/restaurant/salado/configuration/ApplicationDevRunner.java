package org.restaurant.salado.configuration;

import org.apache.commons.io.IOUtils;
import org.restaurant.salado.entities.*;
import org.restaurant.salado.repositories.RoleRepository;
import org.restaurant.salado.repositories.UserRepository;
import org.restaurant.salado.services.MealService;
import org.restaurant.salado.services.RestaurantFileService;
import org.restaurant.salado.utils.RestaurantUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.stream.Stream;

/**
 * @author Haytham DAHRI
 * Insert Employees And Admins Users For Development Environment
 */
@Configuration
@Profile("dev")
public class ApplicationDevRunner implements ApplicationRunner {

    private RestaurantFileService restaurantFileService;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private MealService mealService;

    @Autowired
    public void setRestaurantFileService(RestaurantFileService restaurantFileService) {
        this.restaurantFileService = restaurantFileService;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setMealService(MealService mealService) {
        this.mealService = mealService;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        // Check if users are not persisted yet
        if (this.userRepository.findAll().isEmpty()) {
            // Save default image
            File file = new File("uploads/users/images/default.png");
            RestaurantFile restaurantFile = new RestaurantFile(null, file.getName(), RestaurantUtils.getExtensionByApacheCommonLib(file.getName()), MediaType.IMAGE_PNG.toString(), IOUtils.toByteArray(new FileInputStream(file)), null);
            restaurantFile = this.restaurantFileService.saveRestaurantFile(restaurantFile);
            // Save users
            User user = new User(null, "haytham.dahri@gmail.com", this.passwordEncoder.encode("toortoor"),
                    "haytham_dahri", true, false, new Date(), restaurantFile, "Casablanca, Morocco", "", new Date(), null, null, null);
            user = this.userRepository.save(user);
            // Save roles
            Role roleUser = this.roleRepository.save(new Role(null, RoleType.ROLE_USER, null));
            Role roleEmployee = this.roleRepository.save(new Role(null, RoleType.ROLE_EMPLOYEE, null));
            Role roleAdmin = this.roleRepository.save(new Role(null, RoleType.ROLE_ADMIN, null));

            // Add roles to ADMIN previous user
            user.addRole(roleUser);
            user.addRole(roleEmployee);
            user.addRole(roleAdmin);

            // Save the user
            this.userRepository.save(user);
        }

        // Check if meals are not persisted yet
        if (this.mealService.getMeals().isEmpty()) {

            // Persist Meals
            final File file = new File("uploads/meals/images/meal.jpg");
            Stream.of("Beaf", "Chicken Breast", "Milkshake", "Meat", "Papay", "Orange Juice", "Big Mac", "Cheesy", "Coca Cola", "Fanta", "Riko").forEach(name -> {
                RestaurantFile restaurantFileStream = null;
                try {
                    RestaurantFile restaurantFile = new RestaurantFile(null, file.getName(), RestaurantUtils.getExtensionByApacheCommonLib(file.getName()), MediaType.IMAGE_JPEG_VALUE, IOUtils.toByteArray(new FileInputStream(file)), null);
                    restaurantFile = this.restaurantFileService.saveRestaurantFile(restaurantFile);
                    Meal meal = new Meal(null, name, restaurantFile, BigDecimal.valueOf(75L), 150L, 896L, BigDecimal.valueOf(70L), false, null, null);
                    this.mealService.saveMeal(meal);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
