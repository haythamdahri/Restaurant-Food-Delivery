package org.restaurant.salado;

import org.apache.commons.io.IOUtils;
import org.restaurant.salado.entities.*;
import org.restaurant.salado.repositories.RoleRepository;
import org.restaurant.salado.repositories.UserRepository;
import org.restaurant.salado.services.MealService;
import org.restaurant.salado.services.RestaurantFileService;
import org.restaurant.salado.utils.RestaurantUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
 * @author Haytam DAHRI
 */
@SpringBootApplication
public class RestaurantDeliveryServiceBackendApplication implements CommandLineRunner {

    @Autowired
    private RestaurantFileService restaurantFileService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MealService mealService;

    public static void main(String[] args) {
        SpringApplication.run(RestaurantDeliveryServiceBackendApplication.class, args);
    }

    /**
     * Business logic to execute after application start
     *
     * @param args: Arguments
     * @throws Exception: Thrown on many scenarios
     */
    @Override
    @Transactional
    public void run(String... args) throws Exception {
//        // Save default image
//        File file = new File("uploads/users/images/default.png");
//        RestaurantFile restaurantFile = new RestaurantFile(null, file.getName(), RestaurantUtils.getExtensionByApacheCommonLib(file.getName()), MediaType.IMAGE_PNG.toString(), IOUtils.toByteArray(new FileInputStream(file)), null);
//        restaurantFile = this.restaurantFileService.saveRestaurantFile(restaurantFile);
//        // Save users
//        User user = new User(null, "haytham.dahri@gmail.com", this.passwordEncoder.encode("toortoor"),
//                "haytham_dahri", true, restaurantFile, "Casablanca, Morocco", "", new Date(), null, null, null);
//
//        user = this.userRepository.save(user);
//
//
//        // Save roles
//        Role roleUser = this.roleRepository.save(new Role(null, RoleType.ROLE_USER, null));
//        Role roleEmployee = this.roleRepository.save(new Role(null, RoleType.ROLE_EMPLOYEE, null));
//        Role roleAdmin = this.roleRepository.save(new Role(null, RoleType.ROLE_ADMIN, null));
//
//        // Add roles to ADMIN previous user
//        user.addRole(roleUser);
//        user.addRole(roleEmployee);
//        user.addRole(roleAdmin);
//
//        // Save the user
//        this.userRepository.save(user);
//
//        // Persist Meals
//        file = new File("uploads/meals/images/meal.jpg");
//        File finalFile = file;
//        Stream.of("Beaf", "Chicken Breast", "Milkshake", "Meat", "Papay", "Orange Juice", "Big Mac", "Cheesy", "Coca Cola", "Fanta", "Riko").forEach(name -> {
//            RestaurantFile restaurantFileStream = null;
//            try {
//                restaurantFileStream = new RestaurantFile(null, finalFile.getName(), RestaurantUtils.getExtensionByApacheCommonLib(finalFile.getName()), MediaType.IMAGE_JPEG_VALUE, IOUtils.toByteArray(new FileInputStream(finalFile)), null);
//                Meal meal = new Meal(null, name, restaurantFileStream, BigDecimal.valueOf(75L), 150L, 896L, BigDecimal.valueOf(70L), null, null);
//                this.mealService.saveMeal(meal);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
    }

}
