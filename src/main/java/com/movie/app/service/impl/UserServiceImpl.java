package com.movie.app.service.impl;

import com.movie.app.dto.RegistrationRequest;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


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
    public BaseResponse signUp(RegistrationRequest registrationRequest) {

        BaseResponse response  = new BaseResponse();
        try {

            validateRequest(registrationRequest);

            entityFinderUtilityService.checkIfEmailExist(registrationRequest.getEmail());

            User user = entityFinderUtilityService.createUser(registrationRequest);

            keycloakService.registerUserOnKeycloak(registrationRequest.getUsername(), registrationRequest.getEmail(), registrationRequest.getPassword(), registrationRequest.getFirstname(), registrationRequest.getLastname());

            String tokenLink = confirmTokenService.generateToken(user);

            // Log the generated token for debugging
            if (tokenLink != null && !tokenLink.isEmpty()) {
                LOGGER.info("Generated confirmation token: {}", tokenLink);
            } else {
                LOGGER.warn("Token generation returned null or empty.");
            }

            String fullName = registrationRequest.getFirstname() + " " + registrationRequest.getLastname();

            String emailPayload = emailService.buildEmail(fullName, tokenLink);

            emailService.sendEmail(user.getEmail(), emailPayload);

            return responseService.createSuccessResponse(response, "User registration is successful");

        } catch (InvalidPhoneNumberException ex) {
            LOGGER.error("Invalid phone number provided: {} - Error: {}", registrationRequest.getPhoneNumber(), ex.getMessage());
            return responseService.createErrorResponse(response, "Invalid phone number", HttpServletResponse.SC_BAD_REQUEST);

        } catch (InvalidPasswordException ex) {
            LOGGER.error("Invalid password provided: {} - Error: {}", registrationRequest.getPassword(), ex.getMessage());
            return responseService.createErrorResponse(response, "Invalid password", HttpServletResponse.SC_BAD_REQUEST);

        } catch (PasswordMismatchException ex) {
            LOGGER.error("Password mismatch for user: {} - Error: {}", registrationRequest.getUsername(), ex.getMessage());
            return responseService.createErrorResponse(response, "Passwords do not match", HttpServletResponse.SC_BAD_REQUEST);

        } catch (AccountAlreadyExistsException ex) {
            LOGGER.error("Account already exists for email: {} - Error: {}", registrationRequest.getEmail(), ex.getMessage());
            return responseService.createErrorResponse(response, "Username already taken", HttpServletResponse.SC_BAD_REQUEST);

        } catch (Exception ex) {
            LOGGER.error("Unexpected error during signup for user: {} - Error: {}", registrationRequest.getUsername(), ex.getMessage());
            throw new UserSignupException("An unexpected error occurred during signup. Please try again later.", ex);
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
