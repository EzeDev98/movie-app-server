package com.movie.app.utility.impl;

import com.movie.app.dto.RegistrationRequest;
import com.movie.app.exception.AccountAlreadyExistsException;
import com.movie.app.exception.TokenNotFoundException;
import com.movie.app.exception.UserNotFoundException;
import com.movie.app.model.ConfirmToken;
import com.movie.app.model.User;
import com.movie.app.repositories.ConfirmTokenRepository;
import com.movie.app.repositories.UserRepository;
import com.movie.app.utility.EntityUtilityService;
import com.movie.app.utility.PasswordManagerService;
import com.movie.app.utility.ValidateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EntityUtilityServiceImpl implements EntityUtilityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityUtilityServiceImpl.class);
    private final UserRepository userRepository;
    private final ValidateService validateService;
    private final ConfirmTokenRepository confirmTokenRepository;
    private final PasswordManagerService passwordManagerService;

    public EntityUtilityServiceImpl(UserRepository userRepository, ValidateService validateService, ConfirmTokenRepository confirmTokenRepository, PasswordManagerService passwordManagerService) {
        this.userRepository = userRepository;
        this.validateService = validateService;
        this.confirmTokenRepository = confirmTokenRepository;
        this.passwordManagerService = passwordManagerService;
    }

    @Override
    public void checkIfEmailExist(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            LOGGER.warn("Attempt to create an account with an existing email: {}", email);
            throw new AccountAlreadyExistsException("User with email " + email + " already exists!");
        });
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
    }

    @Override
    public ConfirmToken findByToken(String token) {
        return confirmTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException("Token not found: " + token));
    }

    @Override
    public void setLogin(Long id) {
        User user = findUserById(id);

        if (!isLoggedIn(user)) {
            user.setStatus(true);
            userRepository.save(user);
        }
    }

    @Override
    public void enableUser(String email) {
        User user = findUserByEmail(email);
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public void unLockUser(String email) {
        User user = findUserByEmail(email);
        user.setLocked(false);
        userRepository.save(user);
    }

    @Override
    public User createUser(RegistrationRequest request) {
        User user = new User();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordManagerService.encryptPassword(request.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Override
    public boolean isLoggedIn(User user) {
        return userRepository.findById(user.getId()).
                map(User::isStatus)
                .orElse(false);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
    }

}
