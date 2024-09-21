package dat.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dat.dtos.GenreDTO;
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

    public static final String API_KEY = System.getenv("API_KEY") != null ? System.getenv("API_KEY").trim() : "";
    private static final String GENRE_API_URL = "https://api.themoviedb.org/3/genre/movie/list?api_key=" + API_KEY + "&language=en-US";

    public static List<Genre> fetchAndSaveGenres(EntityManager entityManager) throws IOException, InterruptedException {
        if (API_KEY.isEmpty()) {
            throw new IllegalArgumentException("API key is missing or not set in environment variables");
        }

        // Fetch genres from API
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(GENRE_API_URL)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Log the API response to check it
        System.out.println("API Response: " + response.body());

        ObjectMapper objectMapper = new ObjectMapper();
        List<GenreDTO> genreDTOs = objectMapper.readValue(
                objectMapper.treeAsTokens(objectMapper.readTree(response.body()).get("genres")),
                objectMapper.getTypeFactory().constructCollectionType(List.class, GenreDTO.class)
        );

        // Konverter GenreDTO til Genre entiteter
        List<Genre> genres = genreDTOs.stream()
                .map(dto -> new Genre(dto.getId(), dto.getName(), null))  // Map GenreDTO til Genre entity
                .collect(Collectors.toList());

        GenreDAO genreDAO = new GenreDAO(entityManager);

        entityManager.getTransaction().begin();
        genres.forEach(genre -> {
            // Brug altid merge - uanset om genren er ny eller eksisterende
            Genre mergedGenre = entityManager.merge(genre);
            System.out.println("Genre persisted or merged: " + mergedGenre.getName());
        });
        entityManager.getTransaction().commit();

        return genres;
    }
}
