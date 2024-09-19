package dat.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dat.dtos.GenreDTO;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)  // Ignore any unknown fields (like status_code or others)
public class GenreResponse {
    private List<GenreDTO> genres;  // List of genres from the API

    public List<GenreDTO> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreDTO> genres) {
        this.genres = genres;
    }
}
