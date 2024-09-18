package dat.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dat.dtos.MovieDTO;
import dat.entities.Movie;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class MovieService {

    // API key is fetched from the environment variable
    public static final String API_KEY = System.getenv("API_KEY");
    public static final String BASE_URL_MOVIE = "https://api.themoviedb.org/3/movie/";
    public static final String FETCH_DANISH_MOVIES = "https://api.themoviedb.org/3/discover/movie";

    public static MovieDTO getAllDanishMovies() throws IOException, InterruptedException {

        String release_year = "2019-01-01";
        // Ensure that API_KEY is set
        if (API_KEY == null || API_KEY.isEmpty()) {
            throw new IllegalArgumentException("API key is not set in environment variables.");
        }

        ArrayList<Movie> allMovies = new ArrayList<>(); // To store all movies
        int currentPage = 1;
        int totalPages;

        do {
            String url = FETCH_DANISH_MOVIES + "?api_key=" + API_KEY +
                    "&region=DK&sort_by=release_date.desc&primary_release_date.gte="
            + release_year + "&with_original_language=da&page=" + currentPage;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());


            // Parse the response and extract MovieDTO
            MovieDTO movieDTO = objectMapper.readValue(response.body(), MovieDTO.class);

            // Add the movies from this page to the allMovies list
            allMovies.addAll(movieDTO.getMovies());

            // Get the total number of pages from the first request
            totalPages = movieDTO.getTotalPages();

            currentPage++; // Move to the next page
        } while (currentPage <= totalPages); // Keep going until we fetch all pages

        // Create a final MovieDTO object with all movies
        return MovieDTO.builder()
                .movies(allMovies)
                .page(totalPages)  // Not really needed, but keeping it for reference
                .build();

    }
}
