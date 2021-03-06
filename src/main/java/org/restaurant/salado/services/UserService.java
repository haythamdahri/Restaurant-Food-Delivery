package org.restaurant.salado.services;

import org.restaurant.salado.dtos.UserDTO;
import org.restaurant.salado.entities.User;
import org.restaurant.salado.exceptions.BusinessException;
import org.restaurant.salado.models.PasswordReset;
import org.restaurant.salado.models.UserRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Haytam DAHRI
 */
public interface UserService {

    User registerUser(UserDTO userDTO) throws IOException;

    User saveUser(User user);

    User updateUserOnlineStatus(Long id, boolean online);

    User saveUser(Long id, UserRequest userRequest);

    Boolean requestUserPasswordReset(String email);

    Boolean resetUserPassword(PasswordReset passwordReset);

    Boolean activateAccount(String token);

    User enableAccount(Long id) throws BusinessException;

    User disableAccount(Long id) throws BusinessException;

    User getUser(Long id);

    Boolean checkUserTokenValidity(String token);

    User updateUserImage(MultipartFile multipartFile, String email) throws IOException;

    User updateUserEmail(String currentEmail, String newEmail);

    User addOrRemoveMealFromUserPreferences(String email, Long mealId);

    User getEnabledUser(String email);

    User getUser(String email);

    User getUserByToken(String token);

    boolean deleteUser(Long id);

    List<User> getUsers();

    List<User> getUsers(String exclusionCriteria);

    List<User> getBasicUsers(String search);

    Page<User> getBasicUsersPage(String search, int page, int size);

}
