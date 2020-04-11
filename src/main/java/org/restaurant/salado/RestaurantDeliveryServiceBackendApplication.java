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
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.Date;

/**
 * @author Haytam DAHRI
 */
@SpringBootApplication
public class RestaurantDeliveryServiceBackendApplication implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RestaurantFileService restaurantFileService;

    @Autowired
    private MealService mealService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(RestaurantDeliveryServiceBackendApplication.class, args);
    }

    /**
     * Business logic to execute after application start
     *
     * @param args
     * @throws Exception
     */
    @Override
    @Transactional
    public void run(String... args) throws Exception {
/*        // Save default image
        File file = new File("uploads/users/images/default.png");
        RestaurantFile restaurantFile = new RestaurantFile(null, file.getName(), RestaurantUtils.getExtensionByApacheCommonLib(file.getName()), MediaType.IMAGE_PNG, IOUtils.toByteArray(new FileInputStream(file)), null);
        restaurantFile = this.restaurantFileService.saveRestaurantFile(restaurantFile);
        System.out.println(restaurantFile);
        // Save users
        User user = new User(null, "haytham.dahri@gmail.com",
                this.bCryptPasswordEncoder.encode("toortoor"),
                "haytham_dahri", true, restaurantFile, "Casablanca, Morocco", "", new Date(), null, null, null);

        user = this.userRepository.save(user);

        // Save roles
        Role role_user = this.roleRepository.save(new Role(null, RoleType.ROLE_USER, null));
        Role role_employee = this.roleRepository.save(new Role(null, RoleType.ROLE_EMPLOYEE, null));
        Role role_admin = this.roleRepository.save(new Role(null, RoleType.ROLE_ADMIN, null));

        // Add roles to th previous user
        user.addRole(role_user);
        user.addRole(role_employee);
        user.addRole(role_admin);

        // Save the user
        this.userRepository.save(user);*/

        // Persist Meals
        /*File file = new File("/home/haytham/Downloads/meal.jpg");
        RestaurantFile restaurantFile = new RestaurantFile(null, file.getName(), RestaurantUtils.getExtensionByApacheCommonLib(file.getName()), MediaType.IMAGE_PNG, IOUtils.toByteArray(new FileInputStream(file)), null);
        Meal meal = new Meal(null, "Crispy Chicken Salad", restaurantFile, BigDecimal.valueOf(75L), 150L, 896L, BigDecimal.valueOf(70L), null, null);
        meal = this.mealService.saveMeal(meal);*/
    }

}
