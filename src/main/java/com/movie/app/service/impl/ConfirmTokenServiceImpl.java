package com.movie.app.service.impl;

import com.movie.app.model.ConfirmToken;
import com.movie.app.model.User;
import com.movie.app.repositories.ConfirmTokenRepository;
import com.movie.app.service.ConfirmTokenService;
import com.movie.app.service.KeycloakService;
import com.movie.app.utility.EntityUtilityService;
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

//        Date expirationDate = Date.from(expirationTime.atZone(ZoneId.systemDefault()).toInstant());

        String token = keycloakService.getKeycloakToken(user.getUsername(), user.getPassword());

        ConfirmToken confirmToken = new ConfirmToken(token, LocalDateTime.now(), expirationTime, user);

        saveToken(confirmToken);

        String link = LINK + token;

        return link;
    }

    @Transactional
    @Override
    public String confirmToken(String token) {
        ConfirmToken confirmToken = getToken(token);

        if (confirmToken == null) {
            throw new IllegalStateException("Token not found");
        }

        if (confirmToken.getConfirmedAt() != null) {
            return "redirect:/confirmed";
        }

        LocalDateTime expiresAt = confirmToken.getExpiresAt();

        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired");
        }

        setConfirmedAt(token);

        entityUtilityService.enableUser(confirmToken.getUser().getEmail());

        entityUtilityService.unLockUser(confirmToken.getUser().getEmail());

        return "redirect:/login";
    }

    private void setConfirmedAt(String token) {
        ConfirmToken confirmToken = entityUtilityService.findByToken(token);
        confirmToken.setConfirmedAt(LocalDateTime.now());
    }
}
