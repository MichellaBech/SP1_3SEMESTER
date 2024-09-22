package dat.daos;

import dat.entities.Genre;
import dat.persistence.IDAO;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class GenreDAO implements IDAO<Genre> {
    private EntityManager entityManager;

    public GenreDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Genre> getByMovieId(int id) {
        // Denne metode er sandsynligvis ikke relevant for Genre, men hvis det er nødvendigt,
        // kan den returnere en tom Optional eller ændres til at søge efter et Genre, hvis det giver mening i din sammenhæng.
        return Optional.empty();
    }

    @Override
    public List<Genre> getAll() {
        // Henter alle genre-objekter fra databasen
        return entityManager.createQuery("SELECT g FROM Genre g", Genre.class).getResultList();
    }

    @Override
    public void save(Genre genre) {
        if (genre.getId() != null && entityManager.contains(genre)) {
            entityManager.merge(genre);  // Opdaterer eksisterende entitet
        } else {
            entityManager.persist(genre);  // Indsætter ny entitet
        }
    }

    @Override
    public void update(Genre genre) {
        try {
            entityManager.merge(genre);  // Bruger merge til at opdatere genren
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update genre", e);
        }
    }

    @Override
    public void delete(Genre genre) {
        try {
            // Hvis entityManager ikke allerede indeholder genren, skal den først merge.
            if (!entityManager.contains(genre)) {
                genre = entityManager.merge(genre);
            }
            entityManager.remove(genre);  // Fjerner genren fra databasen
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete genre", e);
        }
    }

    public Genre findByName(String name) {
        try {
            return entityManager.createQuery("SELECT g FROM Genre g WHERE g.name = :name", Genre.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (Exception e) {
            return null;  // Ingen genre fundet med det givne navn
        }
    }

    public Genre findById(Long id) {
        try {
            return entityManager.find(Genre.class, id);  // Finder genre efter ID
        } catch (Exception e) {
            return null;  // Ingen genre fundet med det givne ID
        }
    }
}
