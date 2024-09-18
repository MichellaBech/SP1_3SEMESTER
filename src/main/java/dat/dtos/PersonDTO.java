package dat.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {

        private String knownForDepartment;
        private String name;
        private List<KnownFor> knownFor;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class KnownFor {
            private String title;
        }

}

