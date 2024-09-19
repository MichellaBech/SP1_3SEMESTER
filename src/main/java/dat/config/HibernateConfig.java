package dat.config;

import dat.entities.Genre;
import dat.entities.Movie; // Import your Movie entity

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

/**
 * Purpose: This class is used to configure Hibernate and create an EntityManagerFactory.
 *
 */
public class HibernateConfig {
    private static EntityManagerFactory emf;
    private static boolean isIntegrationTest = false;

    public static void setTestMode(boolean isTest) {
        HibernateConfig.isIntegrationTest = isTest;
    }

    private static EntityManagerFactory emfTest;

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null)
            emf = createEMF(false);
        return emf;
    }

    public static EntityManagerFactory getEntityManagerFactoryForTest() {
        if (emfTest == null)
            emfTest = createEMF(true);
        return emfTest;
    }

    // Register your entity classes here for them to be recognized by Hibernate
    private static void getAnnotationConfiguration(Configuration configuration) {
        configuration.addAnnotatedClass(Movie.class);
        configuration.addAnnotatedClass(Genre.class);
    }

    private static EntityManagerFactory createEMF(boolean forTest) {
        try {
            Configuration configuration = new Configuration();
            Properties props = new Properties();

            // Set the properties based on the environment
            setBaseProperties(props);

            if (forTest || isIntegrationTest) {
                props = setTestProperties(props);
            } else if (System.getenv("DEPLOYED") != null) {
                setDeployedProperties(props);
            } else {
                props = setDevProperties(props);
            }

            configuration.setProperties(props);
            getAnnotationConfiguration(configuration); // Register entities

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
            SessionFactory sf = configuration.buildSessionFactory(serviceRegistry);

            return sf.unwrap(EntityManagerFactory.class);
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // Fetch the database name from properties or environment variables
    private static String getDBName() {
        return "moviedatabase"; // Hardcode database name for now
    }

    // Common properties for Hibernate configuration
    private static Properties setBaseProperties(Properties props) {
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.put("hibernate.connection.driver_class", "org.postgresql.Driver");
        props.put("hibernate.hbm2ddl.auto", "update"); // Automatically update the schema
        props.put("hibernate.current_session_context_class", "thread");
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.format_sql", "true");
        props.put("hibernate.use_sql_comments", "true");
        return props;
    }

    // Settings for deployed environments (production)
    private static Properties setDeployedProperties(Properties props) {
        props.setProperty("hibernate.connection.url", System.getenv("CONNECTION_STR") + getDBName());
        props.setProperty("hibernate.connection.username", System.getenv("DB_USERNAME"));
        props.setProperty("hibernate.connection.password", System.getenv("DB_PASSWORD"));
        return props;
    }

    // Settings for development environment
    private static Properties setDevProperties(Properties props) {
        props.put("hibernate.connection.url", "jdbc:postgresql://localhost:5432/" + getDBName());
        props.put("hibernate.connection.username", "postgres"); // Change to your dev username
        props.put("hibernate.connection.password", "postgres");  // Change to your dev password
        return props;
    }

    // Settings for test environment
    private static Properties setTestProperties(Properties props) {
        props.put("hibernate.connection.driver_class", "org.testcontainers.jdbc.ContainerDatabaseDriver");
        props.put("hibernate.connection.url", "jdbc:tc:postgresql:15.3-alpine3.18:///test_db");
        props.put("hibernate.connection.username", "postgres");
        props.put("hibernate.connection.password", "postgres");
        props.put("hibernate.archive.autodetection", "class");
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.hbm2ddl.auto", "create-drop"); // Recreate schema for test cases
        return props;
    }
}
