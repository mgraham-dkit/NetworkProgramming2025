package tcp.json_based_movie_service.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MovieManager implements IMovieManager{
    private static int movieIdCount = 0;
    private final HashMap<Integer, Movie> movies;

    public MovieManager(){
        this.movies = new HashMap<>();
    }

    public boolean add(String name, int year, String genre){
        Movie m = new Movie(movieIdCount, name, year, genre);
        movieIdCount++;

        return add(m);
    }

    public boolean add(Movie m){
        boolean added = false;
        if(!movies.containsKey(m.getId())) {
            added = true;
            movies.put(m.getId(), m);
        }
        return added;
    }

    public Movie remove(int id){
        return movies.remove(id);
    }

    public Movie search(int id){
        return movies.get(id);
    }

    public List<Movie> getAllMovies(){
        return new ArrayList<>(movies.values());
    }
}
