package com.movie.app.service.impl;

import com.movie.app.exception.AuthenticationException;
import com.movie.app.service.AuthService;
import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final KeycloakServiceImpl keycloak;

    public AuthServiceImpl(KeycloakServiceImpl keycloak) {
        this.keycloak = keycloak;
    }

    @Override
    public AccessTokenResponse authenticateUser(String phoneNumber, String password) {
        try {
            AccessTokenResponse accessTokenResponse = keycloak.KeycloakAuthentication(phoneNumber, password);
            return accessTokenResponse;
        } catch (Exception ex) {
            LOGGER.error("Error during keycloak authentication", ex.getMessage());
            throw new AuthenticationException("Authentication failed");
        }
    }
}
