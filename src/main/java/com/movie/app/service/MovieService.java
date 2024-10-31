package com.movie.app.service;

import com.movie.app.dto.MovieDto;
import com.movie.app.model.Movies;
import com.movie.app.model.YBMovies;

import java.util.List;

public interface MovieService {
    void addMovie(MovieDto movie);
    List<YBMovies> getMovieFromYouTube(String title);
}
