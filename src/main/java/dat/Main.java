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
            MovieDTO movieDTO = MovieService.fetchMoviesAndAssociateGenres(genreDAO);

            // Step 3: Save movies to the database
            entityManager.getTransaction().begin();
            movieDTO.getMovies().forEach(movie -> {
                movieDAO.save(movie);
                System.out.println("Movie saved: " + movie.getTitle());
            });
            entityManager.getTransaction().commit();

            // After committing the transaction, call the getAll() method to fetch all movies
            List<Movie> allMovies = movieDAO.getAll();

            // Print out all the movies
            System.out.println("All movies from the database:");
            for (Movie movie : allMovies) {
                System.out.println("Title: " + movie.getTitle());
            }

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
