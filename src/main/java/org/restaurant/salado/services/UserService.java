package org.restaurant.salado.services;

import org.restaurant.salado.dtos.UserDTO;
import org.restaurant.salado.entities.User;
import org.restaurant.salado.models.MessageType;
import org.restaurant.salado.models.PasswordReset;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Haytam DAHRI
 */
public interface UserService {

    User registerUser(UserDTO userDTO) throws IOException;

    User saveUser(User user);

    MessageType requestUserPasswordReset(String email);

    MessageType resetUserPassword(PasswordReset passwordReset);

    MessageType activateAccount(String token);

    User getUser(Long id);

    MessageType CheckUserTokenValidity(String token);

    MessageType updateUserImage(MultipartFile multipartFile, String email) throws IOException;

    MessageType updateUserEmail(String currentEmail, String newEmail);

    MessageType addOrRemoveMealFromUserPreferences(String email, Long mealId);

    User getEnabledUser(String email);

    User getUser(String email);

    User getUserByToken(String token);

    boolean deleteUser(Long id);

    List<User> getUsers();

}
