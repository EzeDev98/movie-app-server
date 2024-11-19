package com.movie.app.service.impl;

import com.movie.app.exception.TokenAlreadyConfirmedException;
import com.movie.app.exception.TokenExpiredException;
import com.movie.app.exception.TokenNotConfirmedException;
import com.movie.app.exception.TokenNotFoundException;
import com.movie.app.model.ConfirmToken;
import com.movie.app.model.User;
import com.movie.app.repositories.ConfirmTokenRepository;
import com.movie.app.response.TokenResponse;
import com.movie.app.service.ConfirmTokenService;
import com.movie.app.service.KeycloakService;
import com.movie.app.utility.EntityUtilityService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.movie.app.constants.GeneralConstants.LINK;

@Service
public class ConfirmTokenServiceImpl implements ConfirmTokenService {
    private final ConfirmTokenRepository confirmTokenRepository;
    private final KeycloakService keycloakService;
    private final EntityUtilityService entityUtilityService;

    public ConfirmTokenServiceImpl(ConfirmTokenRepository confirmTokenRepository, KeycloakService keycloakService, EntityUtilityService entityUtilityService) {
        this.confirmTokenRepository = confirmTokenRepository;
        this.keycloakService = keycloakService;
        this.entityUtilityService = entityUtilityService;
    }


    @Override
    public void saveToken(ConfirmToken token) {
        confirmTokenRepository.save(token);
    }

    @Override
    public ConfirmToken getToken(String token) {
        ConfirmToken confirmToken = entityUtilityService.findByToken(token);
        return confirmToken;
    }

    @Override
    public String generateToken(User user) {

        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);

        String token = keycloakService.authenticateAndGetToken(user.getUsername(), user.getPassword());

        System.out.println("Token" + token);

        ConfirmToken confirmToken = new ConfirmToken(token, LocalDateTime.now(), expirationTime, user);

        saveToken(confirmToken);

        String link = LINK + token;

        return link;
    }

    @Transactional
    @Override
    public TokenResponse confirmToken(String token) {
        ConfirmToken confirmToken = getToken(token);

        if (confirmToken == null) {
            throw new TokenNotFoundException("Token not found");
        }

        if (confirmToken.getConfirmedAt() != null) {
            throw new TokenAlreadyConfirmedException("Token has already been confirmed");
        }

        LocalDateTime expiresAt = confirmToken.getExpiresAt();
        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Token expired");
        }

        setConfirmedAt(token);

        entityUtilityService.enableUser(confirmToken.getUser().getEmail());
        entityUtilityService.unLockUser(confirmToken.getUser().getEmail());

        return new TokenResponse("Click on the link below to sign in to your account", HttpServletResponse.SC_OK, true);
    }

    private void setConfirmedAt(String token) {
        ConfirmToken confirmToken = entityUtilityService.findByToken(token);
        if (confirmToken !=null ) {
            confirmToken.setConfirmedAt(LocalDateTime.now());
            confirmTokenRepository.save(confirmToken);
        } else {
            throw new TokenNotFoundException("Token not found during confirmation.");
        }

    }
}
