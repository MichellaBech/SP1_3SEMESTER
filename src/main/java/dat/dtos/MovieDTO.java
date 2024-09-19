package dat.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dat.entities.Movie;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDTO {

    private int id;
    private String overview;
    private String releaseDate;
    private String title;

    @JsonProperty("genre_ids")
    private List<Long> genreIds;  // List to capture genre IDs from the API

    @JsonProperty("results")
    @Builder.Default
    private List<Movie> movies = new ArrayList<>();

    @JsonProperty("total_pages")
    private int totalPages;

    @JsonProperty("total_results")
    private int totalResults;
}
