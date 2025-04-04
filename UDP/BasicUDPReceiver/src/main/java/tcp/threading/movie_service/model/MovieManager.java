package tcp.threading.movie_service.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MovieManager {
    private static int movieIdCount = 0;
    private HashMap<Integer, Movie> movies;

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

    public List<Movie> getAllMovies(){
        return new ArrayList<>(movies.values());
    }
}
