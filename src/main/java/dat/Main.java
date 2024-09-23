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

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        EntityManager entityManager = emf.createEntityManager();

        MovieDAO movieDAO = new MovieDAO(entityManager);
        GenreDAO genreDAO = new GenreDAO(entityManager);

        try {
            List<Genre> genres = GenreService.fetchAndSaveGenres(entityManager);

            MovieDTO movieDTO = MovieService.fetchMoviesAndAssociateGenres(genreDAO);

            entityManager.getTransaction().begin();
            movieDTO.getMovies().forEach(movie -> {
                movieDAO.save(movie);
                System.out.println("Movie saved: " + movie.getTitle());
            });
            entityManager.getTransaction().commit();

            List<Movie> allMovies = movieDAO.getAll();

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
