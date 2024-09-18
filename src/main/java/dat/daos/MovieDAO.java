package dat.daos;

import dat.entities.Movie;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class MovieDAO {
    private final EntityManager entityManager;

    public MovieDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Optional<Movie> getById(int id) {
        Movie movie = entityManager.find(Movie.class, id);
        return Optional.ofNullable(movie);
    }

    public List<Movie> getAll() {
        return entityManager.createQuery("SELECT m FROM Movie m", Movie.class).getResultList();
    }

    // Use merge to update or insert a movie
    public void save(Movie movie) {
        try {
            entityManager.merge(movie);  // Use merge instead of persist
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to merge movie", e);
        }
    }

    public void update(Movie movie) {
        try {
            entityManager.merge(movie);  // Use merge for updating
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update movie", e);
        }
    }

    public void delete(Movie movie) {
        try {
            entityManager.remove(movie);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete movie", e);
        }
    }
}
