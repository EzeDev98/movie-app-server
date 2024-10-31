package com.movie.app.service;

import org.keycloak.representations.AccessTokenResponse;

public interface AuthService {
    AccessTokenResponse authenticateUser(String phoneNumber, String password);
}
