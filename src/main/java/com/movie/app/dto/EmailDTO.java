package com.movie.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDTO {

    private String recipient;

    private String link;

}
