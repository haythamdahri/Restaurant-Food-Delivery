package org.restaurant.salado.controllers;

import org.restaurant.salado.entities.*;
import org.restaurant.salado.models.PasswordReset;
import org.restaurant.salado.services.*;
import org.restaurant.salado.utils.RestaurantUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping(value = "/api/v1")
@CrossOrigin(value = "*")
public class RestaurantRestController {

    @Autowired
    private MealService mealService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MealOrderService mealOrderService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RestaurantUtils restaurantUtils;

    @Value("${default_user_image}")
    private String DEFAULT_USER_IMAGE;

    @Value("${token_expiration}")
    private int tokenExpiration;

    @Value("${upload_dir}")
    private String UPLOAD_DIR;

    private static final List<String> imageContentTypes = Arrays.asList("image/png", "image/jpeg", "image/gif");


    /*
     * Sign up end point
     */
    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    public ResponseEntity<User> registerUser(@RequestBody User user) {

        // Generate user token and expiration date
        // Add user role
        // Set user default image
        user.setImage(this.DEFAULT_USER_IMAGE);
        user.setRoles(new ArrayList<>());
        user.addRole(this.roleService.getRole(RoleType.ROLE_USER));
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        user.setExpiryDate(user.calculateExpiryDate(this.tokenExpiration));
        user.setEnabled(false);
        // Hash the password
        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
        // Create user account
        user = this.userService.saveUser(user);
        // Send activation email
        this.emailService.sendActivationEmail(user.getToken(), user.getEmail(), "Account Activation");
        // Return success message response
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /*
     * Reset Password Request Endpoint
     */
    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    public ResponseEntity<Map> resetPasswordRequest(@RequestBody String email) {
        System.out.println(email);
        Map<Object, Object> data = new HashMap<>();
        // Retrieve user
        User user = this.userService.getUser(email.toString());
        if (user != null && user.isEnabled()) {
            // Generate token and expiry date
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setExpiryDate(user.calculateExpiryDate(this.tokenExpiration));
            // Save user
            user = this.userService.saveUser(user);
            // Send password reset email
            this.emailService.sendResetPasswordEmail(user.getToken(), user.getEmail(), "Password Reset");
            // Set response data
            data.put("status", true);
            data.put("message", "Reset password email has been sent to " + user.getEmail() + ".");
        } else if (user != null && !user.isEnabled()) {
            data.put("status", false);
            data.put("message", "Account not enabled yet!");
        } else {
            data.put("status", false);
            data.put("message", "Email does not belong to any user or email not enabled yet!");
        }
        // Return response
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    /*
     * Reset Password Post Endpoint
     */
    @RequestMapping(value = "/perform-reset-password", method = RequestMethod.POST)
    public ResponseEntity<Map> performResetPassword(@RequestBody PasswordReset passwordReset) {
        Map<Object, Object> data = new HashMap<>();
        // Retrieve user
        User user = this.userService.getUserByToken(passwordReset.getToken());
        // Check user existing
        if (user != null && user.isEnabled()) {
            // Check token expiration
            if (!user.isValidToken()) {

            } else {
                String hashedPassword = this.bCryptPasswordEncoder.encode(passwordReset.getNewPassword());
                user.setPassword(hashedPassword);
                user.setToken(null);
                user.setExpiryDate(null);
                this.userService.saveUser(user);
                // Send email
                this.emailService.sendResetPasswordCompleteEmail(user.getToken(), user.getEmail(), "Password Changed");
                // Set success data
                data.put("status", true);
                data.put("message", "Password has been changed successfully");
            }
        } else if (user != null && !user.isEnabled()) {
            data.put("status", false);
            data.put("message", "Account not enabled yet");
        } else {
            data.put("status", false);
            data.put("message", "Invalid token!");
        }
        // Return response
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    /*
     * Activate account Endpoint
     */
    @RequestMapping(value = "/activate-account/{token}", method = RequestMethod.GET)
    public ResponseEntity<Map> activateAccount(@PathVariable(name = "token") String token) {
        Map<Object, Object> data = new HashMap<>();
        // Retrieve user based on received token
        User user = this.userService.getUserByToken(token);

        // Check if user exists or return invalid token
        if (user == null) {
            data.put("status", false);
            data.put("message", "Invalid token");
        } else {
            // Check if account already activated
            if (user.isEnabled()) {
                data.put("status", false);
                data.put("message", "account already enabled");
            } else {
                // Check token validity
                if (user.isValidToken()) {
                    user.setEnabled(true);
                    user = this.userService.saveUser(user);
                    data.put("status", true);
                    data.put("message", "account enabled");
                } else {
                    data.put("status", false);
                    data.put("message", "token expired");
                }
            }
        }
        // Return final response
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    /*
     * Check token validity endpoint
     */
    @RequestMapping(value = "/check-token/{token}", method = RequestMethod.GET)
    public ResponseEntity<Map> checkToken(@PathVariable(name = "token") String token) {
        Map<Object, Object> data = new HashMap<>();
        boolean status;
        String message = "";
        // Retrieve user from token
        User user = this.userService.getUserByToken(token);
        // Check if user exists
        if (user == null) {
            status = false;
            message = "Token does not belong to any user";
        } else {
            // Check token valdity
            if (!user.isValidToken()) {
                status = false;
                message = "token expired";
            } else {
                status = true;
                message = "Valid token";
            }
        }
        // Response data
        data.put("status", status);
        data.put("message", message);
        // Return response
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Map<String, String> home() {
        Map<String, String> data = new HashMap<>();
        data.put("Name", "HAYTHAM DAHRI");
        return data;
    }

    /*
     * Retrieve all meals Endpoint
     */
    @RequestMapping(value = "/meals", method = RequestMethod.GET)
    public ResponseEntity<Collection<Meal>> getAllMealsEndPoint() {
        return new ResponseEntity<>(this.mealService.getMeals(), HttpStatus.OK);
    }

    /*
     * Meal post request handler
     */
    @RequestMapping(value = "/meals", method = RequestMethod.POST)
    public ResponseEntity<Meal> postMeal(@RequestBody Meal meal) {
        meal = this.mealService.saveMeal(meal);
        return new ResponseEntity<>(meal, HttpStatus.OK);
    }

    /*
     * Add meal order to user orders
     * @temp set orders for first user
     */
    @RequestMapping("/add-user-meal-order")
    public ResponseEntity<Object> addUserOder(@RequestBody MealOrder mealOrder, Authentication authentication) {
        // Fetch connected user from database
        User user = this.userService.getUser(authentication.getName());
        // Get or create user order cart
        Collection<Order> userOrders = this.orderService.getUserOrders(user.getId());
        // Get last order
        Optional<Order> optionalOrder = userOrders.stream().filter(order -> !order.isCancelled() && !order.isDelivered()).findFirst();
        // Create a new user order
        Order userOrder;
        // Check if user has already a waiting order
        if (optionalOrder.isPresent()) {
            userOrder = optionalOrder.get();
        } else {
            userOrder = new Order(null, user, null, 0, "", new Date(), false, false);
        }
        // Check if meal already exists in cart
        if (userOrder.getMealOrders() != null) {
            for (MealOrder ml : userOrder.getMealOrders()) {
                if (ml.getMeal().getId() == mealOrder.getMeal().getId()) {
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("message", "Selected meal already exists in your cart!");
                    errorResponse.put("error", "true");
                    return new ResponseEntity<>(errorResponse, HttpStatus.OK);
                }
            }
        }
        // Save order
        userOrder.setCancelled(false);
        userOrder.setDelivered(false);
        userOrder.calculateTotalPrice();
        userOrder = this.orderService.saveOrder(userOrder);
        // Calculate price then save mealOrder
        mealOrder.setPrice(mealOrder.getQuantity() * mealOrder.getMeal().getPrice());
        mealOrder.setOrder(userOrder);
        mealOrder = this.mealOrderService.saveMealOrder(mealOrder);
        // Return response entity
        return new ResponseEntity<>(userOrder, HttpStatus.OK);
    }

    /*
     * User cart
     * @temp using user with id=1
     */
    @RequestMapping(value = "/user-cart", method = RequestMethod.GET)
    public ResponseEntity<Map> getUserCart(Authentication authentication) {
        // Fetch connected user from database
        User user = this.userService.getUser(authentication.getName());
        // Create results data
        Map<Object, Object> data = new HashMap<>();
        // Get user orders
        Collection<Order> orders = this.orderService.getUserOrders(user.getId());
        // Get last active order
        Order userActiveOrder = this.orderService.getLastActiveOrder(user.getId());
        // Check if their is an active order
        if (userActiveOrder == null) {
            data.put("status", true);
            data.put("noActiveOrder", true);
        } else {
            userActiveOrder.calculateTotalPrice();
            userActiveOrder = this.orderService.saveOrder(userActiveOrder);
            data.put("status", true);
            data.put("activeOrder", userActiveOrder);
        }
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    /*
     * Upload user picture endpoint
     */
    @RequestMapping(value = "/edit-user-image", method = RequestMethod.POST)
    public ResponseEntity<Map> uploadUserImage(@RequestParam(name = "image") MultipartFile file, Authentication authentication) {
        Map<Object, Object> data = new HashMap<>();
        boolean status;
        User user;
        String message;
        // Retrieve connected user
        String email = authentication.getName();
        user = this.userService.getUser(email);
        // Check if user exists
        try {
            if (user == null) {
                throw new Exception();
            } else {
                if (file.isEmpty() || !imageContentTypes.contains(file.getContentType())) {
                    message = "Invalid user image";
                    status = false;
                } else {
                    user.setImage(user.getId() + "." + this.restaurantUtils.getExtensionByApacheCommonLib(file.getOriginalFilename()));
                    // Save the user on the database
                    user = this.userService.saveUser(user);
                    // Upload user image or update it if exists
                    byte[] bytes = file.getBytes();
                    Path path = Paths.get(this.UPLOAD_DIR + "/users/images/" + user.getImage());
                    Files.write(path, bytes);
                    // Set message
                    status = true;
                    message = "User image has been changed successfully";
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            status = false;
            message = "an error occurred, please try again";
        }
        data.put("status", status);
        data.put("message", message);
        data.put("user", user);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    /*
     * Upload user picture endpoint
     */
    @RequestMapping(value = "/update-email", method = RequestMethod.POST)
    public ResponseEntity<Map> uploadUserImage(@RequestParam(name = "email") String newEmail, Authentication authentication) {
        Map<Object, Object> data = new HashMap<>();
        boolean status;
        User user;
        String message;
        // Retrieve connected user
        String email = authentication.getName();
        user = this.userService.getUser(email);
        // Check if user exists
        try {
            if (user == null) {
                throw new Exception();
            } else {
                // Update user email
                String token = UUID.randomUUID().toString();
                user.setToken(token);
                user.setExpiryDate(user.calculateExpiryDate(this.tokenExpiration));
                user.setEnabled(false);
                user.setEmail(newEmail);
                // Save user
                user = this.userService.saveUser(user);
                // Send activation email
                this.emailService.sendActivationEmail(token, user.getEmail(), "Email confirmation");
                // Set message
                status = true;
                message = "Email has been updated successfully, please confirm your email via recieved mail";
            }
        } catch (Exception ex) {
            status = false;
            message = "An error occurred, please try again!";
        }
        // Set response data
        data.put("status", status);
        data.put("message", message);
        // Return response
        return new ResponseEntity<>(data, HttpStatus.OK);
    }


}
