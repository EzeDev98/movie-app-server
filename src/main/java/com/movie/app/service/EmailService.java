package com.movie.app.service;

import com.movie.app.dto.EmailDTO;
import com.movie.app.exception.EmailException;
import com.movie.app.response.MailJetResponse;

public interface EmailService {
    MailJetResponse sendEmail(String email, String link) throws EmailException;
    String buildEmail(String name, String link);
}