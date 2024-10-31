package com.movie.app.utility;

public interface PasswordManagerService {
    void validatePassword(String passwordProvided, String actualPassword);
    String encryptPassword(String password);
    boolean isValid(String password);
    boolean isPasswordMatch(String password, String confirmPassword);
}
