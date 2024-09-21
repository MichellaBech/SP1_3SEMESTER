package dat.daos;

import dat.entities.Movie;
import dat.persistence.IDAO;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class MovieDAO implements IDAO<Movie> {
    private final EntityManager entityManager;

    public MovieDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Movie> getByMovieId(int id) {
        Movie movie = entityManager.find(Movie.class, id);
        return Optional.ofNullable(movie);
    }

    @Override
    public List<Movie> getAll() {
        return entityManager.createQuery("SELECT m FROM Movie m", Movie.class).getResultList();
    }

    @Override
    public void save(Movie movie) {
        entityManager.merge(movie);  // Use merge for both new and existing entities
    }

    @Override
    public void update(Movie movie) {
        try {
            entityManager.merge(movie);  // Use merge for updating
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update movie", e);
        }
    }

    @Override
    public void delete(Movie movie) {
        try {
            entityManager.remove(movie);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete movie", e);
        }
    }
}
