package tcp.movie_service;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Movie {
    private int id;
    private String name;
    private int year;
    private String genre;
}
