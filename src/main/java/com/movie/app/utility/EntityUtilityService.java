package com.movie.app.utility;

import com.movie.app.dto.RegistrationRequest;
import com.movie.app.model.ConfirmToken;
import com.movie.app.model.User;

public interface EntityUtilityService {
    void checkIfEmailExist(String email);
    User findUserByEmail(String email);
    ConfirmToken findByToken(String token);
    User findUserById(Long id);
    void setLogin(Long id);
    void enableUser(String email);
    void unLockUser(String email);
    User createUser(RegistrationRequest request);
    boolean isLoggedIn(User user);
    User findByUsername(String username);
}
