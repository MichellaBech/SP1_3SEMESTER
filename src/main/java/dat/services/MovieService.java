package dat.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dat.dtos.MovieDTO;
import dat.entities.Genre;
import dat.entities.Movie;
import dat.daos.GenreDAO;
import jakarta.persistence.EntityManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MovieService {

    public static final String API_KEY = System.getenv("API_KEY");
    public static final String FETCH_DANISH_MOVIES = "https://api.themoviedb.org/3/discover/movie";

    public static MovieDTO fetchMoviesAndAssociateGenres(GenreDAO genreDAO) throws IOException, InterruptedException {

        String release_year = "2019-01-01";
        ArrayList<Movie> allMovies = new ArrayList<>();
        int currentPage = 1;
        int totalPages;

        do {
            String url = FETCH_DANISH_MOVIES + "?api_key=" + API_KEY +
                    "&region=DK&sort_by=release_date.desc&primary_release_date.gte="
                    + release_year + "&with_original_language=da&page=" + currentPage;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            MovieDTO movieDTO = objectMapper.readValue(response.body(), MovieDTO.class);

            // Process each movie and associate genres
            List<Movie> moviesWithGenres = movieDTO.getMovies().stream().map(movie -> {
                // Fetch genres for the movie by matching genre IDs with the saved genres in the DB
                if (movie.getGenreIds() != null) {
                    List<Genre> genres = movie.getGenreIds().stream()
                            .map(genreDAO::findById)  // Fetch the Genre entity from DB by ID
                            .filter(genre -> genre != null)  // Filter out any null values
                            .collect(Collectors.toList());
                    movie.setGenres(genres);
                } else {
                    System.out.println("No genres found for movie: " + movie.getTitle());
                }
                return movie;
            }).collect(Collectors.toList());

            allMovies.addAll(moviesWithGenres);
            totalPages = movieDTO.getTotalPages();
            currentPage++;
        } while (currentPage <= totalPages);

        return MovieDTO.builder()
                .movies(allMovies)
                .build();
    }
}
