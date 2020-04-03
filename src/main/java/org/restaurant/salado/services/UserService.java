package org.restaurant.salado.services;

import org.restaurant.salado.entities.User;

import java.util.List;

public interface UserService {

    User saveUser(User user);

    User getUser(Long id);

    User getEnabledUser(String email);

    User getUser(String email);

    User getUserByToken(String token);

    boolean deleteUser(Long id);

    List<User> getUsers();

}
