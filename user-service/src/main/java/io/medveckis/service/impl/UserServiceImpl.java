package io.medveckis.service.impl;

import io.medveckis.model.User;
import io.medveckis.repository.UserRepository;
import io.medveckis.service.UserService;
import io.medveckis.web.dto.UserData;
import io.micronaut.core.annotation.Nullable;
import jakarta.inject.Singleton;

@Singleton
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Integer createUser(UserData userData) {
        return userRepository.save(convert(userData)).getId();
    }

    @Override
    @Nullable
    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    private User convert(UserData userData) {
        User user = new User();
        user.setFirstName(userData.getFirstName());
        user.setLastName(userData.getLastName());
        user.setEmail(userData.getEmail());
        user.setAge(userData.getAge());
        user.setRole(userData.getRole());
        user.setLoyaltyLevel(1);
        return user;
    }
}
