package org.restaurant.salado.services.impl;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.restaurant.salado.dtos.UserDTO;
import org.restaurant.salado.entities.Meal;
import org.restaurant.salado.entities.RestaurantFile;
import org.restaurant.salado.entities.RoleType;
import org.restaurant.salado.entities.User;
import org.restaurant.salado.mappers.UserMapper;
import org.restaurant.salado.models.MessageType;
import org.restaurant.salado.models.PasswordReset;
import org.restaurant.salado.providers.Constants;
import org.restaurant.salado.repositories.UserRepository;
import org.restaurant.salado.services.*;
import org.restaurant.salado.utils.RestaurantUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Haytam DAHRI
 */
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private RestaurantFileService restaurantFileService;

    private RoleService roleService;

    private UserMapper userMapper;

    private BCryptPasswordEncoder passwordEncoder;

    private EmailService emailService;

    private MealService mealService;

    @Value("${token_expiration}")
    private int tokenExpiration;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRestaurantFileService(RestaurantFileService restaurantFileService) {
        this.restaurantFileService = restaurantFileService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Autowired
    public void setMealService(MealService mealService) {
        this.mealService = mealService;
    }

    @Override
    public User registerUser(UserDTO userDTO) throws IOException {
        // Map UserDTO to User
        User user = this.userMapper.toModel(userDTO);
        // Generate user token and expiration date
        // Add user role
        // Set user default image
        File file = new File("uploads/users/images/default.png");
        RestaurantFile restaurantFile = new RestaurantFile(null, FilenameUtils.removeExtension(file.getName()), RestaurantUtils.getExtensionByApacheCommonLib(file.getName()), MediaType.IMAGE_PNG.toString(), IOUtils.toByteArray(new FileInputStream(file)), null);
        restaurantFile = this.restaurantFileService.saveRestaurantFile(restaurantFile);
        user.setImage(restaurantFile);
        user.setRoles(new HashSet<>());
        user.addRole(this.roleService.getRole(RoleType.ROLE_USER));
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        user.setExpiryDate(user.calculateExpiryDate(this.tokenExpiration));
        user.setEnabled(false);
        // Hash the password
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        // Create user account
        user = this.userRepository.save(user);
        // Send activation email
        this.emailService.sendActivationEmail(user.getToken(), user.getUserId().getEmail(), "Account Activation");
        // Return created user
        return user;
    }

    @Override
    public MessageType requestUserPasswordReset(String email) {
        // Create MessageType
        MessageType messageType;
        // Retrieve user
        User user = this.userRepository.findByUserIdEmail(email).orElse(null);
        if (user != null && user.isEnabled()) {
            // Generate token and expiry date
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setExpiryDate(user.calculateExpiryDate(this.tokenExpiration));
            // Save user
            user = this.userRepository.save(user);
            // Send password reset email
            this.emailService.sendResetPasswordEmail(user.getToken(), user.getUserId().getEmail(), "Password Reset");
            // Set success data
            messageType = MessageType.PASSWORD_RESET_EMAIL_SENT;
        } else if (user != null && !user.isEnabled()) {
            messageType = MessageType.ACCOUNT_NOT_ENABLED;
        } else {
            messageType = MessageType.EMAIL_NOT_FOUND;
        }
        // Return data
        return messageType;
    }

    @Override
    public MessageType resetUserPassword(PasswordReset passwordReset) {
        // Create MessageType
        MessageType messageType;
        // Retrieve user
        User user = this.userRepository.findByToken(passwordReset.getToken()).orElse(null);
        // Check user existing
        if (user != null && user.isEnabled()) {
            // Check token expiration
            if (!user.isValidToken()) {
                messageType = MessageType.EXPIRED_TOKEN;
            } else {
                String hashedPassword = this.passwordEncoder.encode(passwordReset.getNewPassword());
                user.setPassword(hashedPassword);
                user.setToken(null);
                user.setExpiryDate(null);
                this.userRepository.save(user);
                // Send email
                this.emailService.sendResetPasswordCompleteEmail(user.getUserId().getEmail(), "Password Changed");
                // Set success data
                messageType = MessageType.PASSWORD_CHANGED_SUCCESSFULLY;
            }
        } else if (user != null && !user.isEnabled()) {
            // Set Account Not Enabled Token
            messageType = MessageType.ACCOUNT_NOT_ENABLED;
        } else {
            // Set Invalid Token
            messageType = MessageType.INVALID_TOKEN;
        }
        // Return MessageType
        return messageType;
    }

    @Override
    public MessageType activateAccount(String token) {
        // Create MessageType
        MessageType messageType;
        // Retrieve user based on received token
        User user = this.userRepository.findByToken(token).orElse(null);
        // Check if user exists or return invalid token
        if (user == null) {
            messageType = MessageType.INVALID_TOKEN;
        } else {
            // Check if account already activated
            if (user.isEnabled()) {
                messageType = MessageType.ACCOUNT_ALREADY_ENABLED;
            } else {
                // Check token validity
                if (user.isValidToken()) {
                    user.setEnabled(true);
                    this.userRepository.save(user);
                    messageType = MessageType.ACCOUNT_ENABLED_SUCCESSFULLY;
                } else {
                    messageType = MessageType.EXPIRED_TOKEN;
                }
            }
        }
        return messageType;
    }

    @Override
    public MessageType CheckUserTokenValidity(String token) {
        // Create MessageType
        MessageType messageType;
        // Retrieve user from token
        User user = this.userRepository.findByToken(token).orElse(null);
        // Check if user exists
        if (user == null) {
            messageType = MessageType.INVALID_TOKEN;
        } else {
            // Check token validity
            if (!user.isValidToken()) {
                messageType = MessageType.EXPIRED_TOKEN;
            } else {
                messageType = MessageType.VALID_TOKEN;
            }
        }
        // Response data
        return messageType;
    }

    @Override
    public MessageType updateUserImage(MultipartFile file, String email) throws IOException {
        // Create MessageType
        MessageType messageType;
        // Retrieve user
        User user = this.userRepository.findByUserIdEmail(email).orElse(null);
        if (user == null) {
            messageType = MessageType.ERROR;
        } else {
            if (file.isEmpty() || !Constants.IMAGE_CONTENT_TYPES.contains(file.getContentType())) {
                messageType = MessageType.INVALID_USER_IMAGE;
            } else {
                // Update user image file and link it with current user
                RestaurantFile restaurantFile = this.restaurantFileService.saveRestaurantFile(file, user.getImage());
                user.setImage(restaurantFile);
                // Save the user on the database
                this.userRepository.save(user);
                // Set message
                messageType = MessageType.USER_IMAGE_UPDATED_SUCCESSFULLY;
            }
        }
        // Return data
        return messageType;
    }

    @Override
    public MessageType updateUserEmail(String currentEmail, String newEmail) {
        // Create MessageType
        MessageType messageType;
        // Retrieve user
        User user = this.userRepository.findByUserIdEmail(currentEmail).orElse(null);
        // Check if user exists
        if (user == null) {
            messageType = MessageType.ERROR;
        } else {
            // Update user email
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setExpiryDate(user.calculateExpiryDate(this.tokenExpiration));
            user.setEnabled(false);
            user.getUserId().setEmail(newEmail);
            // Save user
            user = this.userRepository.save(user);
            // Send user email update mail
            this.emailService.sendUpdateUserMailEmail(token, user.getUserId().getEmail(), "Email confirmation");
            // Set message
            messageType = MessageType.EMAIL_UPDATED;
        }
        // Return data
        return messageType;
    }

    @Override
    public MessageType addOrRemoveMealFromUserPreferences(String email, Long mealId) {
        // Create MessageType
        MessageType messageType;
        Meal meal = this.mealService.getMeal(mealId);
        User user = this.userRepository.findByUserIdEmail(email).orElse(null);
        // Assert user not null
        assert user != null;
        boolean preferred = user.addOrRemoveMealFromUserPreferences(meal);
        // Save user
        this.userRepository.save(user);
        // success data
        messageType = preferred ? MessageType.MEAL_ADDED_TO_PREFERENCES : MessageType.MEAL_REMOVED_FROM_PREFERENCES;
        // Return response
        return messageType;
    }

    @Override
    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    @Override
    @Transactional
    public User getUser(Long id) {
        Optional<User> optionalUser = this.userRepository.findById(id);
        return optionalUser.orElse(null);
    }

    @Override
    @Transactional
    public User getEnabledUser(String email) {
        Optional<User> optionalUser = this.userRepository.findByUserIdEmailAndEnabledIsTrue(email);
        return optionalUser.orElse(null);
    }

    @Override
    public User getUser(String email) {
        return this.userRepository.findByUserIdEmail(email).orElse(null);
    }

    @Override
    public User getUserByToken(String token) {
        Optional<User> optionalUser = this.userRepository.findByToken(token);
        return optionalUser.orElse(null);
    }

    @Override
    public boolean deleteUser(Long id) {
        this.userRepository.deleteById(id);
        return true;
    }

    @Override
    public List<User> getUsers() {
        return this.userRepository.findAll();
    }
}
