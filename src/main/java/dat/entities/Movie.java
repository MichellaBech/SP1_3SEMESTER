package dat.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "movies") // Ensure table name matches your database schema
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Ensure IDs are auto-generated
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @JsonProperty("release_date")
    @Column(name = "release_date")
    private String releaseDate;


}
