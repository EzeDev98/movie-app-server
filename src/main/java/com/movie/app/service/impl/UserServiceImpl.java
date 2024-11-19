package com.movie.app.service.impl;

import com.movie.app.dto.RegistrationRequest;
import com.movie.app.dto.UserRequest;
import com.movie.app.exception.*;
import com.movie.app.model.User;
import com.movie.app.repositories.UserRepository;
import com.movie.app.response.BaseResponse;
import com.movie.app.service.*;
import com.movie.app.utility.EntityUtilityService;
import com.movie.app.utility.PasswordManagerService;
import com.movie.app.utility.ResponseService;
import com.movie.app.utility.ValidateService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserServiceImpl implements UserService {

    private final EntityUtilityService entityFinderUtilityService;
    private final ValidateService validateService;
    private final PasswordManagerService passwordManagerService;
    private final UserRepository userRepository;
    private final KeycloakService keycloakService;
    private final ConfirmTokenService confirmTokenService;
    private final EmailService emailService;
    private final ResponseService responseService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(EntityUtilityService entityFinderUtilityService, ValidateService validateService, PasswordManagerService passwordManagerService, UserRepository userRepository, KeycloakService keycloakService, ConfirmTokenService confirmTokenService, EmailService emailService, ResponseService responseService) {
        this.entityFinderUtilityService = entityFinderUtilityService;
        this.validateService = validateService;
        this.passwordManagerService = passwordManagerService;
        this.userRepository = userRepository;
        this.keycloakService = keycloakService;
        this.confirmTokenService = confirmTokenService;
        this.emailService = emailService;
        this.responseService = responseService;
    }


    @Override
    @Transactional
    public ResponseEntity<BaseResponse> signUp(RegistrationRequest registrationRequest) {

        BaseResponse response  = new BaseResponse();
        try {

            validateRequest(registrationRequest);

            entityFinderUtilityService.checkIfEmailExist(registrationRequest.getEmail());

            keycloakService.registerUserOnKeycloak(registrationRequest.getUsername(), registrationRequest.getFirstname(), registrationRequest.getLastname(), registrationRequest.getEmail(), registrationRequest.getPassword());

            User user = entityFinderUtilityService.createUser(registrationRequest);

            String tokenLink = confirmTokenService.generateToken(user);

            String fullName = registrationRequest.getFirstname() + " " + registrationRequest.getLastname();

            String emailPayload = emailService.buildEmail(fullName, tokenLink);

            emailService.sendEmail(user.getEmail(), emailPayload);

            BaseResponse successResponse = responseService.createSuccessResponse(response, "User registration is successful");

            return ResponseEntity.ok(successResponse);

        } catch (InvalidPhoneNumberException ex) {
            LOGGER.error("Invalid phone number provided: {} - Error: {}", registrationRequest.getPhoneNumber(), ex.getMessage());
            BaseResponse errorResponse = responseService.createErrorResponse(response, "Invalid phone number", HttpServletResponse.SC_BAD_REQUEST);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        } catch (InvalidPasswordException ex) {
            LOGGER.error("Invalid password provided: {} - Error: {}", registrationRequest.getPassword(), ex.getMessage());
            BaseResponse errorResponse = responseService.createErrorResponse(response, "Invalid password", HttpServletResponse.SC_BAD_REQUEST);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        } catch (PasswordMismatchException ex) {
            LOGGER.error("Password mismatch for user: {} - Error: {}", registrationRequest.getUsername(), ex.getMessage());
            BaseResponse errorResponse = responseService.createErrorResponse(response, "Passwords do not match", HttpServletResponse.SC_BAD_REQUEST);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        } catch (AccountAlreadyExistsException ex) {
            LOGGER.error("Account already exists for email: {} - Error: {}", registrationRequest.getEmail(), ex.getMessage());
            BaseResponse errorResponse = responseService.createErrorResponse(response, "Username already taken", HttpServletResponse.SC_BAD_REQUEST);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        } catch (Exception ex) {
            LOGGER.error("Unexpected error during signup for user: {} - Error: {}", registrationRequest.getUsername(), ex.getMessage());
            BaseResponse errorResponse = responseService.createErrorResponse(response, "An unexpected error occurred during signup. Please try again later.", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

    }

    @Override
    public BaseResponse login(UserRequest userRequest) {

        BaseResponse response = new BaseResponse();

        String username = userRequest.getUsername();
        String password = userRequest.getPassword();

        try {

            validateService.validateRequiredFields(response, username, password);

            User user = entityFinderUtilityService.findByUsername(username);

            passwordManagerService.isPasswordMatch(password, user.getPassword());

            return responseService.createSuccessResponse(response,"User logged in successfully");

        } catch (InvalidPasswordException ex) {
            LOGGER.error("Invalid password provided: {} - Error: {}", password, ex.getMessage());
            return responseService.createErrorResponse(response,"Invalid password", HttpServletResponse.SC_BAD_REQUEST);

        } catch (UsernameNotFoundException ex) {
            LOGGER.error("User not found: {} - Error: {}", username, ex.getMessage());
            return responseService.createErrorResponse(response,"User not found", HttpServletResponse.SC_NOT_FOUND);

        } catch (Exception ex) {
            LOGGER.error("Unexpected error during signup for user: {} - Error: {}", username, ex.getMessage());
            return responseService.createErrorResponse(response,"Internal server error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }
    }


    private void validateRequest(RegistrationRequest registrationRequest) {
        if (!validateService.validatePhoneNumber(registrationRequest.getPhoneNumber())) {
            throw new InvalidPhoneNumberException(ResponseService.INVALID_PHONE_NUMBER);
        }
        if (!passwordManagerService.isValid(registrationRequest.getPassword())) {
            throw new InvalidPasswordException(ResponseService.INVALID_PASSWORD);
        }
        if (!passwordManagerService.isPasswordMatch(registrationRequest.getPassword(), registrationRequest.getConfirmPassword())) {
            throw new PasswordMismatchException(ResponseService.PASSWORD_MISMATCH);
        }
    }
}
