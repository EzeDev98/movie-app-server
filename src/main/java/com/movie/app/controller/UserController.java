package com.movie.app.controller;
import com.movie.app.dto.RegistrationRequest;
import com.movie.app.dto.UserRequest;
import com.movie.app.response.BaseResponse;
import com.movie.app.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("${app.title}")
@CrossOrigin(origins = "*")
@RestController
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register-user")
    public ResponseEntity<BaseResponse> register(@RequestBody RegistrationRequest registrationRequest) {
       return userService.signUp(registrationRequest);
    }

    @PostMapping("/login-user")
    public ResponseEntity<BaseResponse> login(@RequestBody UserRequest userRequest) {
        BaseResponse response = userService.login(userRequest);
        return ResponseEntity.ok(response);
    }

}
