package dat.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dat.entities.Genre;
import dat.daos.GenreDAO;
import jakarta.persistence.EntityManager;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

public class GenreService {

    public static final String API_KEY = System.getenv("API_KEY");
    private static final String GENRE_API_URL = "https://api.themoviedb.org/3/genre/movie/list?api_key=" + API_KEY + "&language=en-US";

    public static List<Genre> fetchAndSaveGenres(EntityManager entityManager) throws IOException, InterruptedException {
        // Fetch genres from API
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(GENRE_API_URL)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Log the API response to check it
        System.out.println("API Response: " + response.body());

        ObjectMapper objectMapper = new ObjectMapper();
        GenreResponse genreResponse = objectMapper.readValue(response.body(), GenreResponse.class);

        // Check if genres field is null
        if (genreResponse.getGenres() == null) {
            System.err.println("No genres found in the API response. Check API key or response format.");
            return null;  // Or throw an exception, based on your application's needs
        }

        // Convert GenreDTO to Genre entity
        List<Genre> genres = genreResponse.getGenres().stream()
                .map(dto -> new Genre(dto.getId(), dto.getName(), null))  // Map GenreDTO to Genre entity
                .collect(Collectors.toList());

        GenreDAO genreDAO = new GenreDAO(entityManager);

        entityManager.getTransaction().begin();
        genres.forEach(genre -> {
            Genre existingGenre = genreDAO.findById(genre.getId());  // Check if the genre already exists
            if (existingGenre == null) {
                entityManager.persist(genre);  // New genre, persist it
                System.out.println("New Genre persisted: " + genre.getName());
            } else {
                System.out.println("Genre already exists: " + genre.getName());
            }
        });
        entityManager.getTransaction().commit();

        return genres;
    }
}
