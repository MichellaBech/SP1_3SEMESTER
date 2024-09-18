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

    // API key is fetched from the environment variable, fallback to hardcoded key for testing
    public static final String API_KEY = System.getenv("API_KEY") != null ? System.getenv("API_KEY").trim() : "your_api_key_here"; // Trim any whitespace
    public static final String BASE_URL_MOVIE = "https://api.themoviedb.org/3/movie/";
    public static final String FETCH_DANISH_MOVIES = "https://api.themoviedb.org/3/discover/movie";

    public static MovieDTO getAllDanishMovies() throws IOException, InterruptedException {

        String release_year = "2019-01-01";
        // Ensure that API_KEY is set
        if (API_KEY == null || API_KEY.isEmpty()) {
            System.err.println("API Key is not set. Please provide an API key in the environment variable 'API_KEY' or directly in the code.");
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

            // Log the API response body to check if it's valid JSON
            System.out.println("API Response: " + response.body());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            // Try to parse the response and catch any exceptions
            try {
                MovieDTO movieDTO = objectMapper.readValue(response.body(), MovieDTO.class);
                // Add the movies from this page to the allMovies list
                allMovies.addAll(movieDTO.getMovies());

                // Get the total number of pages from the first request
                totalPages = movieDTO.getTotalPages();
                currentPage++; // Move to the next page
            } catch (Exception e) {
                System.err.println("Error parsing API response: " + e.getMessage());
                throw new IOException("Failed to parse API response", e);
            }
        } while (currentPage <= totalPages); // Keep going until we fetch all pages

        // Create a final MovieDTO object with all movies
        return MovieDTO.builder()
                .movies(allMovies)
                .page(totalPages)  // Not really needed, but keeping it for reference
                .build();
    }
}
