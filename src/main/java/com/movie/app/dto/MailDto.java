package com.movie.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MailDto {
    private String to;
    private String subject;
    private String text;
}
