package tcp.movie_service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class MovieTCPServer {
    public static void main(String[] args) {
        try {
            ServerSocket connectionSocket = new ServerSocket(MovieUtilities.PORT);

            MovieManager movieManager = new MovieManager();
            boolean validServerSession = true;
            while(validServerSession){
                Socket clientDataSocket = connectionSocket.accept();
                TCPNetworkLayer networkLayer = new TCPNetworkLayer(clientDataSocket);

                boolean validClientSession = true;
                while(validClientSession) {
                    // Receive a message
                    String request = networkLayer.receive();
                    System.out.println("Request: " + request);

                    String response = MovieUtilities.INVALID;

                    String[] components = request.split(MovieUtilities.DELIMITER);
                    switch (components[0]) {
                        case MovieUtilities.ADD:
                            response = handleAddMovie(components, movieManager);
                            break;
                        case MovieUtilities.LIST:
                            response = handleListMovies(components, movieManager);
                            break;
                        case MovieUtilities.EXIT:
                            response = MovieUtilities.ACK;
                            validClientSession = false;
                            break;
                    }

                    networkLayer.send(response);
                }
                // Shut down communication
                networkLayer.disconnect();
            }
        }catch (IOException e){
            System.out.println("Connection socket cannot be established");
        }
    }

    private static String handleListMovies(String[] components, MovieManager movieManager) {
        String response = null;
        if (components.length == 1) {
            List<Movie> movies = movieManager.getAllMovies();
            if (movies.isEmpty()) {
                response = MovieUtilities.NO_MOVIES_FOUND;
            } else {
                response = serializeMovies(movies);
            }
        }
        return response;
    }

    private static String handleAddMovie(String[] components, MovieManager movieManager) {
        String response = null;
        if(components.length == 4) {
            try {
                String name = components[1];

                int year = Integer.parseInt(components[2]);
                if(year > LocalDate.now().getYear()){
                    response = MovieUtilities.INVALID_YEAR;
                    return response;
                }

                String genre = components[3];

                movieManager.add(name, year, genre);
                response = MovieUtilities.ADDED;
            }catch(NumberFormatException e){
                response = MovieUtilities.NON_NUMERIC;
            }
        }
        return response;
    }

    public static String serializeMovies(List<Movie> movies){
        if(movies == null){
            throw new IllegalArgumentException("Cannot serialise null list of Movies");
        }

        if(movies.isEmpty()){
            return "";
        }

        String serialized = serializeMovie(movies.getFirst());

        for (int i = 1; i < movies.size(); i++) {
            Movie currentMovie = movies.get(i);
            serialized = serialized + MovieUtilities.MOVIE_DELIMITER + serializeMovie(currentMovie);
        }

        return serialized;
    }

    public static String serializeMovie(Movie m){
        if(m == null){
            throw new IllegalArgumentException("Cannot serialise null Movie");
        }

        return m.getId() + MovieUtilities.DELIMITER + m.getName() + MovieUtilities.DELIMITER + m.getYear() + MovieUtilities.DELIMITER + m.getGenre();
    }
}
