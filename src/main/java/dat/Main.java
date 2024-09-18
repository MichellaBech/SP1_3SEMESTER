package dat;

import dat.config.HibernateConfig;
import dat.daos.MovieDAO;
import dat.entities.Movie;
import dat.services.MovieService;
import dat.dtos.MovieDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // Check if API_KEY is correctly set
        System.out.println("API_KEY: " + System.getenv("API_KEY"));

        // Initialize the EntityManagerFactory (for dev or production environment)
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        // Create an EntityManager
        EntityManager entityManager = emf.createEntityManager();

        // Create MovieDAO using the EntityManager
        MovieDAO movieDAO = new MovieDAO(entityManager);

        try {
            // Fetch all Danish movies from the API
            MovieDTO movieDTO = MovieService.getAllDanishMovies();

            // Begin transaction
            entityManager.getTransaction().begin();

            // Iterate over the movies fetched from the API and save them to the database
            for (Movie movie : movieDTO.getMovies()) {
                System.out.println("Saving movie: " + movie.getTitle());
                // Persist the movie without checking if it exists
                movieDAO.save(movie);
                System.out.println("Movie saved: " + movie.getTitle());
            }

            // Commit the transaction after saving all movies
            entityManager.getTransaction().commit();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback(); // Rollback if something fails
            }
        } finally {
            // Close EntityManager
            entityManager.close();

            // Close EntityManagerFactory (optional)
            emf.close();
        }
    }
}
