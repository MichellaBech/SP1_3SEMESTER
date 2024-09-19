package dat.daos;

import dat.entities.Genre;
import jakarta.persistence.EntityManager;

public class GenreDAO {
    private EntityManager entityManager;

    public GenreDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(Genre genre) {
        if (genre.getId() != null && entityManager.contains(genre)) {
            entityManager.merge(genre);  // Update existing entity
        } else {
            entityManager.persist(genre);  // Insert new entity
        }
    }

    public Genre findByName(String name) {
        try {
            return entityManager.createQuery("SELECT g FROM Genre g WHERE g.name = :name", Genre.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (Exception e) {
            return null;  // No genre found with the given name
        }
    }

    public Genre findById(Long id) {
        try {
            return entityManager.find(Genre.class, id);  // Find genre by ID
        } catch (Exception e) {
            return null;  // No genre found with the given ID
        }
    }
}
