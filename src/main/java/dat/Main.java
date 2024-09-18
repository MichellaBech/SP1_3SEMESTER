package dat;

import dat.config.HibernateConfig;
import dat.dtos.MovieDTO;
import dat.entities.Movie;
import dat.services.MovieService;
import jakarta.persistence.EntityManagerFactory;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("movieapi");
    }
}
