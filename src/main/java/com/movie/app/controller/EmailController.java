//package com.movie.app.controller;
//
//import com.movie.app.dto.EmailDTO;
//import com.movie.app.exception.EmailException;
//import com.movie.app.response.MailJetResponse;
//import com.movie.app.service.EmailService;
//import org.springframework.web.bind.annotation.*;
//
//@RequestMapping("${app.title}")
//@CrossOrigin(origins = "*")
//@RestController
//public class EmailController {
//    private final EmailService emailService;
//
//    public EmailController(EmailService emailService) {
//        this.emailService = emailService;
//    }
//
//    @PostMapping("/send-email")
//    public MailJetResponse sendEmail(@RequestBody EmailDTO emailDTO) throws EmailException {
//        return emailService.sendEmail(emailDTO);
//    }
//}
