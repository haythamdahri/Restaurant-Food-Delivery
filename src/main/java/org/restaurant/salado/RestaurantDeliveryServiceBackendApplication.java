package org.restaurant.salado;

import org.restaurant.salado.entities.Role;
import org.restaurant.salado.entities.RoleType;
import org.restaurant.salado.entities.User;
import org.restaurant.salado.repositories.MealOrderRepository;
import org.restaurant.salado.repositories.RoleRepository;
import org.restaurant.salado.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.transaction.Transactional;
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
/*        // Save users
        User user = new User(null, "haytham.dahri@gmail.com",
                this.bCryptPasswordEncoder.encode("toortoor"),
                "haytham_dahri", true, "", "Casablanca, Morocco", "", new Date(), null);

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

    }
}
