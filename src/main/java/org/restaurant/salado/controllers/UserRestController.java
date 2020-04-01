package org.restaurant.salado.controllers;

import org.restaurant.salado.entities.RoleType;
import org.restaurant.salado.entities.User;
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
@RequestMapping(path = "/api/v1/users")
@CrossOrigin(value = "*")
public class UserRestController {

    @Autowired
    private UserService userService;

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
    @RequestMapping(value = "/", method = RequestMethod.POST)
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
    @RequestMapping(value = "/passwordreset", method = RequestMethod.GET)
    public ResponseEntity<Map> resetPasswordRequest(@RequestParam(value = "email") String email) {
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
            // Create final user
            User finalUser = user;
            Thread t1 = new Thread(){
                public void run() {
                    // Send password reset email
                    emailService.sendResetPasswordEmail(finalUser.getToken(), finalUser.getEmail(), "Password Reset");
                }
            };
            // Start thread
            t1.start();
            // Set response data
            data.put("status", true);
            data.put("message", "Password reset email has been sent to " + user.getEmail() + ".");
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
    @RequestMapping(value = "/passwordreset", method = RequestMethod.POST)
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
                Thread t1 = new Thread(){
                    public void run() {
                        // Send email
                        emailService.sendResetPasswordCompleteEmail(user.getEmail(), "Password Changed");
                    }
                };
                // Start thread
                t1.start();
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
    @RequestMapping(value = "/activation/{token}", method = RequestMethod.GET)
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
    @RequestMapping(value = "/tokens/{token}/check", method = RequestMethod.GET)
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

    /*
     * Upload user picture endpoint
     */
    @RequestMapping(value = "/image", method = RequestMethod.POST)
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
     * Update user email endpoint
     */
    @RequestMapping(value = "/email", method = RequestMethod.POST)
    public ResponseEntity<Map> updateUserEmail(@RequestParam(name = "email") String newEmail, Authentication authentication) {
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
