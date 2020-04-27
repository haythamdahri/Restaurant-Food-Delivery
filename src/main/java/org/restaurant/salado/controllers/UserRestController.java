package org.restaurant.salado.controllers;

import org.restaurant.salado.dtos.UserDTO;
import org.restaurant.salado.entities.Meal;
import org.restaurant.salado.entities.User;
import org.restaurant.salado.facades.IAuthenticationFacade;
import org.restaurant.salado.models.PasswordReset;
import org.restaurant.salado.providers.Constants;
import org.restaurant.salado.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Haytam DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/users")
@CrossOrigin(value = "*")
@Transactional
public class UserRestController {

    private static final String STATUS = "status";
    private static final String MESSAGE = "message";
    private static final String PREFERRED = "preferred";
    private static final String USER = "user";

    private UserService userService;

    private IAuthenticationFacade authenticationFacade;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setAuthenticationFacade(IAuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    /**
     * Retrieve all users endpoint | Only users with ROLE_USER
     * Authorize only employees and admins to access this endpoint
     * Exclude current authenticated user
     *
     * @return ResponseEntity<List < User>>
     */
    @PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/")
    public ResponseEntity<List<User>> retrieveAllUsers() {
        return ResponseEntity.ok(this.userService.getBasicUsers());
    }

    /**
     * Sign up end point
     *
     * @param userDTO: User Data Transfer Object
     * @return ResponseEntity<User>
     */
    @PostMapping(path = "/")
    public ResponseEntity<User> registerUser(@RequestBody UserDTO userDTO) throws IOException {
        // Create User Account And Return success message response
        return ResponseEntity.ok(this.userService.registerUser(userDTO));
    }

    /**
     * Enable user account
     * Authorize only Employees And Admins
     *
     * @param userId: User Identifier
     * @return ResponseEntity<User>
     */
    @PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/enable")
    public ResponseEntity<User> enableAccount(@RequestParam(value = "id") Long userId) {
        try {
            // Enable account
            return ResponseEntity.ok(this.userService.enableAccount(userId));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Disable user account
     * Authorize only Employees And Admins
     *
     * @param userId: User Identifier
     * @return ResponseEntity<User>
     */
    @PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
    @GetMapping(path = "/disable")
    public ResponseEntity<User> disableAccount(@RequestParam(value = "id") Long userId) {
        try {
            // Enable account
            return ResponseEntity.ok(this.userService.disableAccount(userId));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieve current authenticated user
     *
     * @return ResponseEntity<User>
     */
    @GetMapping(path = "/current")
    public ResponseEntity<User> retrieveAuthenticatedUser() {
        // Fetch user using service tier and Return Response
        return ResponseEntity.ok(this.userService.getUser(this.authenticationFacade.getAuthentication().getName()));
    }

    /**
     * Password Reset Request Endpoint
     *
     * @param email: User Email
     * @return ResponseEntity<ResponseData>
     */
    @GetMapping(path = "/passwordreset")
    public ResponseEntity<Map<String, Object>> resetPasswordRequest(@RequestParam(value = "email") String email) {
        // Create response data
        Map<String, Object> data = new HashMap<>();
        try {
            // Use service to reset user password
            this.userService.requestUserPasswordReset(email);
            // In case of unsuccessful password reset email, a BusinessException will be thrown
            data.put(STATUS, true);
            data.put(MESSAGE, Constants.PASSWORD_RESET_EMAIL_SENT);
        } catch (Exception ex) {
            // Set error response data
            data.put(STATUS, false);
            data.put(MESSAGE, ex.getMessage());
        }
        // Create Response Data And Return response
        return ResponseEntity.ok(data);
    }

    /**
     * Password Reset Post Endpoint
     *
     * @param passwordReset: PasswordReset Object
     * @return ResponseEntity<Map < String, Object>>
     */
    @PostMapping(path = "/passwordreset")
    public ResponseEntity<Map<String, Object>> performResetPassword(@RequestBody PasswordReset passwordReset) {
        // Create response data
        Map<String, Object> data = new HashMap<>();
        try {
            // Use service to reset user password
            this.userService.resetUserPassword(passwordReset);
            // In case of unsuccessful activation, a BusinessException will be thrown
            data.put(STATUS, true);
            data.put(MESSAGE, Constants.PASSWORD_CHANGED_SUCCESSFULLY);
        } catch (Exception ex) {
            // Set error response data
            data.put(STATUS, false);
            data.put(MESSAGE, ex.getMessage());
        }
        // Create Response Data And Return response
        return ResponseEntity.ok(data);
    }

    /**
     * Account activation Endpoint
     *
     * @param token: User token
     * @return ResponseEntity<Map < String, Object>>
     */
    @GetMapping(path = "/activation/{token}")
    public ResponseEntity<Map<String, Object>> activateAccount(@PathVariable(name = "token") String token) {
        // Create response data
        Map<String, Object> data = new HashMap<>();
        try {
            // Use service to activate account
            this.userService.activateAccount(token);
            // In case of unsuccessful activation, a BusinessException will be thrown
            data.put(STATUS, true);
            data.put(MESSAGE, Constants.ACCOUNT_ENABLED_SUCCESSFULLY);
        } catch (Exception ex) {
            // Set error response data
            data.put(STATUS, false);
            data.put(MESSAGE, ex.getMessage());
        }
        // Create Response Data And Return response
        return ResponseEntity.ok(data);
    }

    /**
     * Token validity check Endpoint
     *
     * @param token: User Token
     * @return ResponseEntity<Map < String, Object>>
     */
    @GetMapping(path = "/tokens/{token}/check")
    public ResponseEntity<Map<String, Object>> checkToken(@PathVariable(name = "token") String token) {
        // Create response data
        Map<String, Object> data = new HashMap<>();
        try {
            // Use service to check token validity
            Boolean isValidToken = this.userService.checkUserTokenValidity(token);
            data.put(STATUS, isValidToken);
            data.put(MESSAGE, Constants.VALID_TOKEN);
        } catch (Exception ex) {
            // Invalid token
            data.put(STATUS, false);
            data.put(MESSAGE, ex.getMessage());
        }
        // Create Response Data And Return response
        return ResponseEntity.ok(data);
    }

    /**
     * User image upload Endpoint
     *
     * @param file: User image
     * @return ResponseEntity<Map < String, Object>>
     */
    @PostMapping(path = "/image")
    public ResponseEntity<Map<String, Object>> uploadUserImage(@RequestParam(name = "image") MultipartFile file) {
        // Create response data
        Map<String, Object> data = new HashMap<>();
        try {
            // Use service to upload user image
            User user = this.userService.updateUserImage(file, this.authenticationFacade.getAuthentication().getName());
            // Create Response Data
            data.put(STATUS, true);
            data.put(MESSAGE, Constants.USER_IMAGE_UPDATED_SUCCESSFULLY);
            data.put(USER, user);
        } catch (Exception ex) {
            // Set error response data
            data.put(STATUS, false);
            data.put(MESSAGE, ex.getMessage());
        }
        // Return Response
        return ResponseEntity.ok(data);
    }

    /**
     * User email updaye Endpoint
     *
     * @param newEmail: New user email
     * @return ResponseEntity<Map < String, Object>>
     */
    @PostMapping(path = "/email")
    public ResponseEntity<Map<String, Object>> updateUserEmail(@RequestParam(name = "email") String newEmail) {
        // Create response data
        Map<String, Object> data = new HashMap<>();
        try {
            // Use service to update user email
            this.userService.updateUserEmail(this.authenticationFacade.getAuthentication().getName(), newEmail);
            // Create Response Data
            data.put(STATUS, true);
            data.put(MESSAGE, Constants.USER_IMAGE_UPDATED_SUCCESSFULLY);
        } catch (Exception ex) {
            // Set error response data
            data.put(STATUS, false);
            data.put(MESSAGE, ex.getMessage());
        }
        // Return Response
        return ResponseEntity.ok(data);
    }

    /**
     * User products preferences Endpoint
     *
     * @return ResponseData
     */
    @GetMapping(path = "/preferences")
    public ResponseEntity<List<Meal>> retrieveUserPreferences() {
        // Retrieve user email
        String email = this.authenticationFacade.getAuthentication().getName();
        // Retrieve user preferred meals
        return ResponseEntity.ok(this.userService.getUser(email).getPreferredMeals());
    }

    /**
     * Add or Remove product from User preferences Endpoint
     *
     * @return ResponseEntity<Map < String, Object>>
     */
    @Transactional
    @PostMapping(path = "/preferences")
    public ResponseEntity<Map<String, Object>> toggleProductFromUserPreferences(@RequestParam(value = "id") Long mealId) {
        // Create response data
        Map<String, Object> data = new HashMap<>();
        try {
            // Use service to add or remove meal from user preferences
            User user = this.userService.addOrRemoveMealFromUserPreferences(this.authenticationFacade.getAuthentication().getName(), mealId);
            // Create Response Data And Return response
            data.put(STATUS, true);
            data.put(MESSAGE, user.isMealPreferred(mealId) ? Constants.MEAL_ADDED_TO_PREFERENCES : Constants.MEAL_REMOVED_FROM_PREFERENCES);
            data.put(PREFERRED, user.isMealPreferred(mealId));
        } catch (Exception ex) {
            // Set error response data
            data.put(STATUS, false);
            data.put(MESSAGE, ex.getMessage());
        }
        // Return Response
        return ResponseEntity.ok(data);
    }

}
