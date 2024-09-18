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
    @JsonProperty("results")
    @Builder.Default
    private List<Movie> movies = new ArrayList<>(); // Default to a mutable list

    @JsonProperty("page")
    private int page; // Current page

    @JsonProperty("total_pages")
    private int totalPages; // This should map the total pages from API

    @JsonProperty("total_results")
    private int totalResults; // This s


}

