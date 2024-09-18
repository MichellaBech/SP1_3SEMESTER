package dat.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dat.dtos.MovieDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String movieReleaseDate;

    @ManyToMany
    private List<Genre> genres;

}
