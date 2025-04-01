package tcp.json_based_movie_service.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Movie {
    @EqualsAndHashCode.Include
    private int id;
    private String name;
    private int year;
    private String genre;

    public String format(){
        return id + ": " + name + " - " + genre + " (" + year + ")";
    }
}
