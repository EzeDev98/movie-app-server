package com.movie.app.service;

import com.movie.app.model.ConfirmToken;
import com.movie.app.model.User;
import com.movie.app.response.TokenResponse;

public interface ConfirmTokenService {
    void saveToken(ConfirmToken token);
    ConfirmToken getToken(String token);
    String generateToken(User user);
    TokenResponse confirmToken(String token);
}
