package com.movie.app.controller;
import com.movie.app.dto.RegistrationRequest;
import com.movie.app.response.BaseResponse;
import com.movie.app.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public void register(@RequestBody RegistrationRequest registrationRequest) {
         userService.signUp(registrationRequest);
    }

//    @GetMapping(path = "confirm")
//    public String confirm(@RequestParam("token") String token) {
//        return confirmTokenService.confirmToken(token);
//    }
//
//    @GetMapping("/activate")
//    public String activate() {
//        return "activate";
//    }
//
//    @GetMapping("/confirmed")
//    public String confirmed() {
//        return "confirmed";
//    }
//
//    @GetMapping("/login")
//    public String getLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
//        if (error != null) {
//            model.addAttribute("errorMessage", "Invalid username or password.");
//        }
//        return "login";
//    }
//
//    @GetMapping("/contact")
//    public String contactPage(Model model) {
//        model.addAttribute("page", "contact");
//        return "contact";
//    }
}
