package com.movie.app.controller;

import com.movie.app.dto.MovieDto;
import com.movie.app.model.Movies;
import com.movie.app.model.YBMovies;
import com.movie.app.repositories.MoviesRepository;
import com.movie.app.service.MovieService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Controller
public class MovieController {
    private final MovieService movieService;
    private final MoviesRepository moviesRepository;
    private final static Logger LOGGER = LoggerFactory.getLogger(MovieController.class);

    @GetMapping("/form")
    public String getMovieForm(Model model) {
        model.addAttribute("movies", new MovieDto());
        return "form";
    }

    @PostMapping("/form")
    public String addMovie(@ModelAttribute("movies") @Valid MovieDto movieDto, Model model) {
        try {
            movieService.addMovie(movieDto);
            model.addAttribute("successMessage", "Movie added successfully");
        } catch (Exception ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "form";
        }
        return "form";
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        model.addAttribute("page", "home");
        return "home";
    }

    @GetMapping("/series")
    public String seriesPage(Model model) {
        model.addAttribute("page", "series");
        return "series";
    }

    @GetMapping("/action")
    public String actionPage(Model model) {
        try {
            List<Movies> videos = moviesRepository.findAll();
            model.addAttribute("page", "action");
            model.addAttribute("videos", videos);
        } catch (Exception ex) {
            model.addAttribute("error", "No movies found: " + ex.getMessage());
        }
        return "action";
    }

    @GetMapping("/search")
    public String getMovies(@RequestParam("title") String title, Model model) {

        try {
            List<YBMovies> movies = movieService.getMovieFromYouTube(title);

            if (movies.isEmpty()) {
                model.addAttribute("message", "The movie you just searched for is not available on Youtube at the moment!");
            } else {
                model.addAttribute("movies", movies);
                LOGGER.info("movies: ", movies);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("error", "Error fetching movies: " + ex.getMessage());
        }
        return "search";
    }

    @GetMapping("/play")
    public String getMovieTrailer() {
        return "play";
    }


}
