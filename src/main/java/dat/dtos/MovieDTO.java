package dat.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {

    private List<Genre> genres;
    private int id;
    private String overview;
    private String releaseDate;
    private String title;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Genre {
        private int id;
        private String name;
    }
}

