package com.movie.app.controller;

import com.movie.app.exception.TokenAlreadyConfirmedException;
import com.movie.app.exception.TokenExpiredException;
import com.movie.app.exception.TokenNotFoundException;
import com.movie.app.response.TokenResponse;
import com.movie.app.service.ConfirmTokenService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.title}")
@CrossOrigin(origins = "*")
public class ConfirmTokenController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmTokenController.class);
    private final ConfirmTokenService confirmTokenService;

    public ConfirmTokenController(ConfirmTokenService confirmTokenService) {
        this.confirmTokenService = confirmTokenService;
    }

    @GetMapping(path = "/confirm")
    public ResponseEntity<TokenResponse> confirmToken(@RequestParam String token) {

        try {
            TokenResponse response = confirmTokenService.confirmToken(token);
            return ResponseEntity.status(response.getStatus()).body(response);
        } catch (TokenNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new TokenResponse(e.getMessage(), HttpServletResponse.SC_NOT_FOUND, false));
        } catch (TokenAlreadyConfirmedException e) {
            return ResponseEntity.status(HttpStatus.GONE)
                    .body(new TokenResponse(e.getMessage(), HttpServletResponse.SC_GONE, false));
        } catch (TokenExpiredException e) {
            return ResponseEntity.status(HttpStatus.GONE)
                    .body(new TokenResponse(e.getMessage(), HttpServletResponse.SC_GONE, false));
        } catch (Exception e) {
            LOGGER.error("An error occurred while confirming the token", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new TokenResponse("An unexpected error occurred.", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false));
        }
    }
}
