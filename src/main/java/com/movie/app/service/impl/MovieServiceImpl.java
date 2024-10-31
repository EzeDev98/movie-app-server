package com.movie.app.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.app.dto.MovieDto;
import com.movie.app.model.Movies;
import com.movie.app.model.YBMovies;
import com.movie.app.repositories.MoviesRepository;
import com.movie.app.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class MovieServiceImpl implements MovieService {

    @Value("${youtube.api.key}")
    private String apiKey;

    @Value("${movie.token}")
    private String token;

    private final static String IMAGE_DIRECTORY = "/Users/delsonstech/Desktop/Java_Projects/images/";
    private final MoviesRepository moviesRepository;

    private final static Logger LOGGER = LoggerFactory.getLogger(MovieServiceImpl.class);

    @Autowired
    public MovieServiceImpl(MoviesRepository moviesRepository) {
        this.moviesRepository = moviesRepository;
    }

    @Override
    public void addMovie(MovieDto movie) {

        String imageUrl = uploadImage(movie.getImageUrl());

        System.out.println(imageUrl);

        Movies newMovie = new Movies();

        newMovie.setTitles(movie.getTitle());
        newMovie.setCategories(movie.getCategory());
        newMovie.setGenres(movie.getGenre());
        newMovie.setOrigins(movie.getOrigin());
        newMovie.setDescriptions(movie.getDescription());
        newMovie.setImageUrls(imageUrl);
        newMovie.setYears(movie.getYear());
        newMovie.setVideoUrls(movie.getVideoUrl());
        newMovie.setCreatedAt(LocalDateTime.now());
        moviesRepository.save(newMovie);
    }

    private String uploadImage(MultipartFile image) {

        File directory = new File(IMAGE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filename = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path filepath = Paths.get(IMAGE_DIRECTORY, filename);

        try {
            Files.write(filepath, image.getBytes());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return filename;
    }


    @Override
    public List<YBMovies> getMovieFromYouTube(String title) {

        String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
        String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=" + encodedTitle + "+full+movie&type=video&key=" + apiKey;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("accept", "application/json")
                .timeout(Duration.ofSeconds(20))
                .GET()
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonResponse = objectMapper.readTree(response.body());
                JsonNode items = jsonResponse.get("items");

                List<YBMovies> movies = new ArrayList<>();

                if (items.isArray()) {
                    for (JsonNode movie : items) {

                        JsonNode jsonId = movie.get("id");
                        String movieId = jsonId.path("videoId").asText();

                        if (movieId.isEmpty()) {
                            continue;
                        }

                        JsonNode movieSnippet = movie.get("snippet");
                        String name = movieSnippet.path("title").asText("Unknown Title");
                        String year = movieSnippet.path("publishedAt").asText("Unknown Year");
                        String description = movieSnippet.path("description").asText("No Description Available");
                        String genre = getMovieGenre(name, description);
                        String origin = getMovieOrigin(description);

                        String imageUrl = movieSnippet.path("thumbnails").path("high").path("url").asText(null);

                        String videoUrl = "https://www.youtube.com/watch?v=" + movieId;

                        YBMovies movieDetails = new YBMovies(name, year, imageUrl, videoUrl, origin, genre);
                        movies.add(movieDetails);
                    }
                }
                return movies;
            } else {
                throw new RuntimeException("Failed to fetch movies. Status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    private String getMovieOrigin(String description) {
        if (description.contains("Hollywood") || description.contains("USA")) {
            return "United States";
        } else if (description.contains("Bollywood") || description.contains("India")) {
            return "India";
        } else if (description.contains("Nollywood") || description.contains("Nigeria")) {
            return "Nigeria";
        }
        return "America";
    }

    private String getMovieGenre(String name, String description) {
        String combined = name.toLowerCase() + " " + description.toLowerCase();

        if (combined.contains("action")) {
            return "Action";
        } else if (combined.contains("comedy")) {
            return "Comedy";
        } else if (combined.contains("drama")) {
            return "Drama";
        } else if (combined.contains("horror")) {
            return "Horror";
        } else if (combined.contains("thriller")) {
            return "Thriller";
        }

        return "Action";
    }
}