package com.movie.app.model;

import lombok.Data;

@Data
public class YBMovies {

    private String name;

    private String year;

    private String imageUrl;

    private String videoUrl;

    private String origin;

    private String genre;

    public YBMovies(String name, String year, String imageUrl, String videoUrl, String origin, String genre) {
        this.name = name;
        this.year = year;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.origin = origin;
        this.genre = genre;
    }
}
