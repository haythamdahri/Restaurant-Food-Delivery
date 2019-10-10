package org.restaurant.salado.services;

import org.restaurant.salado.entities.User;
import org.restaurant.salado.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public User getUser(Long id) {
        Optional<User> optionalUser = this.userRepository.findById(id);
        return optionalUser.orElse(null);
    }

    @Override
    public User getEnabledUser(String email) {
        Optional<User> optionalUser = this.userRepository.findByEmailAndEnabledIsTrue(email);
        return optionalUser.orElse(null);
    }

    @Override
    public User getUser(String email) {
        Optional<User> optionalUser = this.userRepository.findByEmail(email);
        if( optionalUser.isPresent() ) {
            return optionalUser.get();
        }
        return null;
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
    public Collection<User> getUsers() {
        return this.userRepository.findAll();
    }
}
