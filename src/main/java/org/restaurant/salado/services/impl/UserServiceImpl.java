package org.restaurant.salado.services.impl;

import org.restaurant.salado.entities.User;
import org.restaurant.salado.repositories.UserRepository;
import org.restaurant.salado.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * @author Haytam DAHRI
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

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
