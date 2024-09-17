package dat.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
}

