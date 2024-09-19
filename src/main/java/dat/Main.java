package dat;

import dat.config.HibernateConfig;
import dat.daos.GenreDAO;
import dat.daos.MovieDAO;
import dat.dtos.MovieDTO;
import dat.entities.Genre;
import dat.entities.Movie;
import dat.services.GenreService;
import dat.services.MovieService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Check if API_KEY is correctly set
        System.out.println("API_KEY: " + System.getenv("API_KEY"));

        // Initialize the EntityManagerFactory (for dev or production environment)
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        // Create DAOs for Movie and Genre
        MovieDAO movieDAO = new MovieDAO(entityManager);
        GenreDAO genreDAO = new GenreDAO(entityManager);

        try {
            // Step 1: Fetch and save genres
            List<Genre> genres = GenreService.fetchAndSaveGenres(entityManager);

            // Step 2: Fetch movies and associate with genres
            MovieDTO movieDTO = MovieService.fetchMoviesAndAssociateGenres(entityManager, genreDAO);

            // Step 3: Save movies to the database
            entityManager.getTransaction().begin();
            movieDTO.getMovies().forEach(movie -> {
                movieDAO.save(movie);
                System.out.println("Movie saved: " + movie.getTitle());
            });
            entityManager.getTransaction().commit();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        } finally {
            entityManager.close();
            emf.close();
        }
    }
}
