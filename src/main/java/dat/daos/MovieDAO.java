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

    // Always use merge to handle both detached and new entities
    public void save(Movie movie) {
        entityManager.merge(movie);  // Use merge for both new and existing entities
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
