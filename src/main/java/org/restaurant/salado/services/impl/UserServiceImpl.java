package org.restaurant.salado.services.impl;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.restaurant.salado.dtos.UserDTO;
import org.restaurant.salado.entities.Meal;
import org.restaurant.salado.entities.RestaurantFile;
import org.restaurant.salado.entities.RoleType;
import org.restaurant.salado.entities.User;
import org.restaurant.salado.exceptions.BusinessException;
import org.restaurant.salado.mappers.UserMapper;
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
        this.emailService.sendActivationEmail(user.getToken(), user.getEmail(), "Account Activation");
        // Return created user
        return user;
    }

    @Override
    public Boolean requestUserPasswordReset(String email) {
        // Retrieve user
        User user = this.userRepository.findByEmail(email).orElse(null);
        if (user != null && user.isEnabled()) {
            // Generate token and expiry date
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setExpiryDate(user.calculateExpiryDate(this.tokenExpiration));
            // Save user
            user = this.userRepository.save(user);
            // Send password reset email
            this.emailService.sendResetPasswordEmail(user.getToken(), user.getEmail(), "Password Reset");
        } else if (user != null && !user.isEnabled()) {
            throw new BusinessException(Constants.ACCOUNT_NOT_ENABLED);
        } else {
            throw new BusinessException(Constants.EMAIL_NOT_FOUND);
        }
        // Return true for successful operation
        return true;
    }

    @Override
    public Boolean resetUserPassword(PasswordReset passwordReset) {
        // Retrieve user
        User user = this.userRepository.findByToken(passwordReset.getToken()).orElse(null);
        // Check user existing
        if (user != null && user.isEnabled()) {
            // Check token expiration
            if (user.isValidToken()) {
                throw new BusinessException(Constants.EXPIRED_TOKEN);
            } else {
                String hashedPassword = this.passwordEncoder.encode(passwordReset.getNewPassword());
                user.setPassword(hashedPassword);
                user.setToken(null);
                user.setExpiryDate(null);
                this.userRepository.save(user);
                // Send email
                this.emailService.sendResetPasswordCompleteEmail(user.getEmail(), "Password Changed");
            }
        } else if (user != null && !user.isEnabled()) {
            // Throw Account Not Enabled Exception Message
            throw new BusinessException(Constants.ACCOUNT_NOT_ENABLED);
        } else {
            // Throw Exception with Invalid Token Message
            throw new BusinessException(Constants.ACCOUNT_NOT_ENABLED);
        }
        // Return true for successful operation
        return true;
    }

    @Override
    public Boolean activateAccount(String token) {
        // Retrieve user based on received token
        User user = this.userRepository.findByToken(token).orElse(null);
        // Check if user exists or return invalid token
        if (user == null) {
            throw new BusinessException(Constants.INVALID_TOKEN);
        } else {
            // Check if account already activated
            if (user.isEnabled()) {
                throw new BusinessException(Constants.ACCOUNT_ALREADY_ENABLED);
            } else {
                // Check token validity
                if (user.isValidToken()) {
                    throw new BusinessException(Constants.EXPIRED_TOKEN);
                } else {
                    user.setEnabled(true);
                    this.userRepository.save(user);
                }
            }
        }
        // Return true for successful operation
        return true;
    }

    @Override
    public Boolean checkUserTokenValidity(String token) {
        // Retrieve user from token
        User user = this.userRepository.findByToken(token).orElse(null);
        // Check if user exists
        if (user == null) {
            throw new BusinessException(Constants.INVALID_TOKEN);
        } else {
            // Check token validity
            if (user.isValidToken()) {
                throw new BusinessException(Constants.EXPIRED_TOKEN);
            }
        }
        // Response true for existed user
        return true;
    }

    @Override
    public User updateUserImage(MultipartFile file, String email) throws IOException {
        // Retrieve user
        User user = this.userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new BusinessException(Constants.ERROR);
        } else {
            if (file.isEmpty() || !Constants.IMAGE_CONTENT_TYPES.contains(file.getContentType())) {
                throw new BusinessException(Constants.INVALID_USER_IMAGE);
            } else {
                // Update user image file and link it with current user
                RestaurantFile restaurantFile = this.restaurantFileService.saveRestaurantFile(file, user.getImage());
                user.setImage(restaurantFile);
                // Save the user on the database
                user = this.userRepository.save(user);
            }
        }
        // Return User
        return user;
    }

    @Override
    public User updateUserEmail(String currentEmail, String newEmail) {
        // Retrieve user
        User user = this.userRepository.findByEmail(currentEmail).orElse(null);
        // Check if user exists
        if (user == null) {
            throw new BusinessException(Constants.ERROR);
        } else {
            // Update user email
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setExpiryDate(user.calculateExpiryDate(this.tokenExpiration));
            user.setEnabled(false);
            user.setEmail(newEmail);
            // Save user
            user = this.userRepository.save(user);
            // Send user email update mail
            this.emailService.sendUpdateUserMailEmail(token, user.getEmail(), "Email confirmation");
        }
        // Return User
        return user;
    }

    @Override
    public User addOrRemoveMealFromUserPreferences(String email, Long mealId) {
        Meal meal = this.mealService.getMeal(mealId);
        User user = this.userRepository.findByEmail(email).orElse(null);
        // Assert user not null
        assert user != null;
        user.addOrRemoveMealFromUserPreferences(meal);
        // Save user
        user = this.userRepository.save(user);
        // Return response
        return user;
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
        Optional<User> optionalUser = this.userRepository.findByEmailAndEnabledIsTrue(email);
        return optionalUser.orElse(null);
    }

    @Override
    public User getUser(String email) {
        return this.userRepository.findByEmail(email).orElse(null);
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
