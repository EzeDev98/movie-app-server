package com.movie.app.utility.impl;

import com.movie.app.exception.InvalidPasswordException;
import com.movie.app.utility.PasswordManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class PasswordManagerServiceImpl implements PasswordManagerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordManagerServiceImpl.class);
    private final PasswordEncoder passwordEncoder;
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])" + "(?=.*[a-z])" + "(?=.*[A-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+$).{8,}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public PasswordManagerServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void validatePassword(String passwordProvided, String actualPassword) {
        if (!passwordEncoder.matches(passwordProvided,actualPassword)) {
            LOGGER.error("Wrong password");
            throw new InvalidPasswordException("Invalid password");
        }
    }

    @Override
    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean isValid(String password) {
        if (password == null) {
            return false;
        }
        return pattern.matcher(password).matches();
    }

    @Override
    public boolean isPasswordMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }
}
