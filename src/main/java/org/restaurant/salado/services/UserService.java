package org.restaurant.salado.services;

import org.restaurant.salado.entities.User;

import java.util.Collection;

public interface UserService {

    public User saveUser(User user);

    public User getUser(Long id);

    public User getEnabledUser(String email);

    public User getUser(String email);

    public User getUserByToken(String token);

    public boolean deleteUser(Long id);

    public Collection<User> getUsers();

}
