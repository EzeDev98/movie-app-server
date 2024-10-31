package com.movie.app.service.impl;

import com.movie.app.exception.KeycloakAuthenticationException;
import com.movie.app.service.KeycloakService;
import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class KeycloakServiceImpl implements KeycloakService {
    private final static Logger LOGGER = LoggerFactory.getLogger("KeycloakService");
    @Value("${keycloak.adminClientId}")
    private String adminClientId;

    @Value("${keycloak.adminClientSecret}")
    private String adminClientSecret;

    @Value("${keycloak.urls.auth}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin.username}")
    private String username;

    @Value("${keycloak.admin.password}")
    private String password;


    private Keycloak getAdminKeycloakInstance() {
        return KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm("master")
                .username(username)
                .password(password)
                .clientId("admin-cli")
                .build();
    }

    @Override
    public void registerUserOnKeycloak(String username, String email, String password){
        Keycloak keycloak = getAdminKeycloakInstance();

        UserRepresentation userRepresentation = getUserRepresentation(username, email, password);

        Response response = keycloak.realm(realm).users().create(userRepresentation);

        if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
            String errorMessage = response.readEntity(String.class);
            LOGGER.warn("Failed to register user: {}", errorMessage);
            throw new KeycloakAuthenticationException("Failed to register user on Keycloak: ");
        }

        LOGGER.info("User successfully registered on Keycloak with username: {}", username);
    }

    private static UserRepresentation getUserRepresentation(String username, String email, String password) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(username);
        userRepresentation.setEmail(email);
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(false);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(password);
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
        return userRepresentation;
    }

    @Override
    public String getKeycloakToken(String username, String password) {
        Keycloak userToken = KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm(realm)
                .clientId(adminClientId)
                .clientSecret(adminClientSecret)
                .username(username)
                .password(password)
                .grantType("password")
                .build();
        return userToken.tokenManager().getAccessToken().getToken();
    }


    @Override
    public AccessTokenResponse keycloakAuthentication(String username, String password) {
        try {
            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(authServerUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .clientId(adminClientId)
                    .clientSecret(adminClientSecret)
                    .username(username)
                    .password(password)
                    .build();
            return keycloak.tokenManager().getAccessToken();
        } catch (Exception ex) {
            throw new KeycloakAuthenticationException("Failed to authenticate with Keycloak");
        }
    }

}
