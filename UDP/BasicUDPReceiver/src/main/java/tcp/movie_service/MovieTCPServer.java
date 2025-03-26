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

            while(true){
                Socket clientDataSocket = connectionSocket.accept();
                // Set up streams to communicate
                Scanner in = new Scanner(clientDataSocket.getInputStream());
                PrintWriter out = new PrintWriter(clientDataSocket.getOutputStream());

                // Receive a message
                String request = in.nextLine();
                System.out.println("Request: " + request);

                String response = MovieUtilities.INVALID;

                String [] components = request.split(MovieUtilities.DELIMITER);
                switch(components[0]){
                    case MovieUtilities.ADD:
                        if(components.length == 4) {
                            try {
                                String name = components[1];

                                int year = Integer.parseInt(components[2]);
                                if(year > LocalDate.now().getYear()){
                                    response = MovieUtilities.INVALID_YEAR;
                                    break;
                                }

                                String genre = components[3];

                                movieManager.add(name, year, genre);
                                response = MovieUtilities.ADDED;
                            }catch(NumberFormatException e){
                                response = MovieUtilities.NON_NUMERIC;
                            }
                        }
                        break;
                    case MovieUtilities.LIST:
                        if(components.length == 1){
                            List<Movie> movies = movieManager.getAllMovies();
                            if(movies.isEmpty()){
                                response = MovieUtilities.NO_MOVIES_FOUND;
                            }else{
                                response = serializeMovies(movies);
                            }
                        }
                        break;
                }

                out.println(response);
                out.flush();

                // Shut down communication
                out.close();
                in.close();

                // Shut down socket
                clientDataSocket.close();

            }
        }catch (IOException e){
            System.out.println("Connection socket cannot be established");
        }
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
