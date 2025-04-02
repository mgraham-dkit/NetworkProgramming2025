package tcp.json_based_movie_service.model;

import java.util.List;

public interface IMovieManager {
    boolean add(String name, int year, String genre);
    boolean add(Movie movie);
    Movie remove(int id);
    Movie search(int id);
    List<Movie> getAllMovies();
}
