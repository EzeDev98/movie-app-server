package com.movie.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
public class MovieDto {

    private String title;
    private String year;
    private String genre;
    private String category;
    private String origin;
    private String description;
    private MultipartFile imageUrl;
    private String videoUrl;

    public MovieDto() {
    }
}
