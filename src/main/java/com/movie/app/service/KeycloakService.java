package com.movie.app.service;

import org.keycloak.representations.AccessTokenResponse;

public interface KeycloakService {
    void registerUserOnKeycloak(String username, String email, String password);
    String getKeycloakToken(String username, String password);
    AccessTokenResponse keycloakAuthentication(String username, String password);
}
