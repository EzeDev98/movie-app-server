package com.movie.app.service;

import com.movie.app.model.ConfirmToken;
import com.movie.app.model.User;

public interface ConfirmTokenService {
    void saveToken(ConfirmToken token);
    ConfirmToken getToken(String token);
    String generateToken(User user);
    String confirmToken(String token);
}
