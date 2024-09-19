package dat.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "genres")
    private List<Movie> movies = new ArrayList<>(); // Initialize as empty list
}


