package org.restaurant.salado.controllers;

import org.restaurant.salado.dtos.UserDTO;
import org.restaurant.salado.entities.Meal;
import org.restaurant.salado.entities.User;
import org.restaurant.salado.facades.IAuthenticationFacade;
import org.restaurant.salado.models.PasswordReset;
import org.restaurant.salado.models.ResponseData;
import org.restaurant.salado.models.MessageType;
import org.restaurant.salado.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

/**
 * @author Haytam DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/users")
@CrossOrigin(value = "*")
@Transactional
public class UserRestController {

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
     * Retrieve all users endpoint
     *
     * @return ResponseEntity<List < User>>
     */
    @GetMapping(path = "/")
    public ResponseEntity<List<User>> retrieveAllUsers() {
        return ResponseEntity.ok(this.userService.getUsers());
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
     * Password Rset Request Endpoint
     *
     * @param email: User Email
     * @return ResponseEntity<ResponseData>
     */
    @GetMapping(path = "/passwordreset")
    public ResponseEntity<ResponseData> resetPasswordRequest(@RequestParam(value = "email") String email) {
        // Use service to request user password email
        MessageType messageType = this.userService.requestUserPasswordReset(email);
        // Create Response Data And Return response
        return ResponseEntity.ok(ResponseData.getInstance(messageType.getMessage(), messageType.getStatus()));
    }

    /**
     * Password Reset Post Endpoint
     *
     * @param passwordReset: PasswordReset Object
     * @return ResponseEntity<ResponseData>
     */
    @PostMapping(path = "/passwordreset")
    public ResponseEntity<ResponseData> performResetPassword(@RequestBody PasswordReset passwordReset) {
        // Use service to perform user password email
        MessageType messageType = this.userService.resetUserPassword(passwordReset);
        // Create Response Data And Return response
        return ResponseEntity.ok(ResponseData.getInstance(messageType.getMessage(), messageType.getStatus()));
    }

    /**
     * Account activation Endpoint
     *
     * @param token: User token
     * @return ResponseEntity<ResponseData>
     */
    @GetMapping(path = "/activation/{token}")
    public ResponseEntity<ResponseData> activateAccount(@PathVariable(name = "token") String token) {
        // Use service to activate account
        MessageType messageType = this.userService.activateAccount(token);
        // Create Response Data And Return response
        return ResponseEntity.ok(ResponseData.getInstance(messageType.getMessage(), messageType.getStatus()));
    }

    /**
     * Token validity check Endpoint
     *
     * @param token: User Token
     * @return ResponseEntity<ResponseData>
     */
    @GetMapping(path = "/tokens/{token}/check")
    public ResponseEntity<ResponseData> checkToken(@PathVariable(name = "token") String token) {
        // Use service to check token validity
        MessageType messageType = this.userService.CheckUserTokenValidity(token);
        // Create Response Data And Return response
        return ResponseEntity.ok(ResponseData.getInstance(messageType.getMessage(), messageType.getStatus()));
    }

    /**
     * User image upload Endpoint
     *
     * @param file: User image
     * @return ResponseEntity<ResponseData>
     */
    @PostMapping(path = "/image")
    public ResponseEntity<ResponseData> uploadUserImage(@RequestParam(name = "image") MultipartFile file) throws IOException {
        // Use service to upload user image
        MessageType messageType = this.userService.updateUserImage(file, this.authenticationFacade.getAuthentication().getName());
        // Create Response Data And Return response
        return ResponseEntity.ok(ResponseData.getInstance(messageType.getMessage(), messageType.getStatus()));
    }

    /**
     * User email updaye Endpoint
     *
     * @param newEmail: New user email
     * @return ResponseEntity<ResponseData>
     */
    @PostMapping(path = "/email")
    public ResponseEntity<ResponseData> updateUserEmail(@RequestParam(name = "email") String newEmail) {
        // Use service to check token validity
        MessageType messageType = this.userService.updateUserEmail(this.authenticationFacade.getAuthentication().getName(), newEmail);
        // Create Response Data And Return response
        return ResponseEntity.ok(ResponseData.getInstance(messageType.getMessage(), messageType.getStatus()));
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
     * @return ResponseEntity<ResponseData>
     */
    @Transactional
    @PostMapping(path = "/preferences")
    public ResponseEntity<ResponseData> toggleProductFromUserPreferences(@RequestParam(value = "id") Long mealId) {
        // Use service to check token validity
        MessageType messageType = this.userService.addOrRemoveMealFromUserPreferences(this.authenticationFacade.getAuthentication().getName(), mealId);
        // Create Response Data And Return response
        return ResponseEntity.ok(ResponseData.getInstance(messageType.getMessage(), messageType.getStatus(), messageType.getExtra()));
    }

}
