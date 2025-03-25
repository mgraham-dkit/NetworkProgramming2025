package udp_movie_service;

import java.util.HashMap;

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
}
