package tcp.threading.movie_service.service;


import tcp.threading.movie_service.model.Movie;
import tcp.threading.movie_service.model.MovieManager;
import tcp.threading.movie_service.network.TCPNetworkLayer;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;

public class ServiceClientHandler implements Runnable{
    private static String username = "admin";
    private static String password = "adminPassword";

    private Socket clientDataSocket;
    private TCPNetworkLayer networkLayer;
    private MovieManager movieManager;

    public ServiceClientHandler(Socket clientDataSocket, MovieManager movieManager) throws IOException {
        this.clientDataSocket = clientDataSocket;
        this.networkLayer = new TCPNetworkLayer(clientDataSocket);

        this.movieManager = movieManager;
    }

    public void run(){
        try {
            boolean validClientSession = true;
            boolean loginStatus = false;
            while (validClientSession) {
                // Receive a message
                String request = networkLayer.receive();
                System.out.println("Request: " + request);

                String response = null;

                String[] components = request.split(tcp.movie_service.MovieUtilities.DELIMITER);
                switch (components[0]) {
                    case tcp.movie_service.MovieUtilities.LOGIN:
                        response = handleLogin(components);
                        if (response.equals(tcp.movie_service.MovieUtilities.SUCCESS)) {
                            loginStatus = true;
                        }
                        break;
                    case tcp.movie_service.MovieUtilities.ADD:
                        response = handleAddMovie(components, movieManager, loginStatus);
                        break;
                    case tcp.movie_service.MovieUtilities.LIST:
                        response = handleListMovies(components, movieManager, loginStatus);
                        break;
                    case tcp.movie_service.MovieUtilities.EXIT:
                        response = tcp.movie_service.MovieUtilities.ACK;
                        validClientSession = false;
                        break;
                }

                if (response == null) {
                    response = tcp.movie_service.MovieUtilities.INVALID;
                }

                networkLayer.send(response);
            }
            // Shut down communication
            networkLayer.disconnect();
        }catch(IOException e)
        {
            System.out.println("ERROR");
        }
    }


    private String handleListMovies(String[] components, MovieManager movieManager, boolean loggedIn) {
        if(!loggedIn)
            return tcp.movie_service.MovieUtilities.NOT_LOGGED_IN;

        String response = null;
        if (components.length == 1) {
            List<Movie> movies = movieManager.getAllMovies();
            if (movies.isEmpty()) {
                response = tcp.movie_service.MovieUtilities.NO_MOVIES_FOUND;
            } else {
                response = serializeMovies(movies);
            }
        }
        return response;
    }

    private String handleLogin(String[] components) {
        String response = null;
        if (components.length == 3) {
            if(components[1].equalsIgnoreCase(username) && components[2].equals(password)){
                response = tcp.movie_service.MovieUtilities.SUCCESS;
            }else{
                response = tcp.movie_service.MovieUtilities.FAILED;
            }
        }
        return response;
    }

    private String handleAddMovie(String[] components, MovieManager movieManager, boolean loggedIn) {
        if(!loggedIn)
            return tcp.movie_service.MovieUtilities.NOT_LOGGED_IN;

        String response = null;
        if(components.length == 4) {
            try {
                String name = components[1];

                int year = Integer.parseInt(components[2]);
                if(year > LocalDate.now().getYear()){
                    response = tcp.movie_service.MovieUtilities.INVALID_YEAR;
                    return response;
                }

                String genre = components[3];

                movieManager.add(name, year, genre);
                response = tcp.movie_service.MovieUtilities.ADDED;
            }catch(NumberFormatException e){
                response = tcp.movie_service.MovieUtilities.NON_NUMERIC;
            }
        }
        return response;
    }

    public String serializeMovies(List<Movie> movies){
        if(movies == null){
            throw new IllegalArgumentException("Cannot serialise null list of Movies");
        }

        if(movies.isEmpty()){
            return "";
        }

        String serialized = serializeMovie(movies.getFirst());

        for (int i = 1; i < movies.size(); i++) {
            Movie currentMovie = movies.get(i);
            serialized = serialized + tcp.movie_service.MovieUtilities.MOVIE_DELIMITER + serializeMovie(currentMovie);
        }

        return serialized;
    }

    public String serializeMovie(Movie m){
        if(m == null){
            throw new IllegalArgumentException("Cannot serialise null Movie");
        }

        return m.getId() + tcp.movie_service.MovieUtilities.DELIMITER + m.getName() + tcp.movie_service.MovieUtilities.DELIMITER + m.getYear() + MovieUtilities.DELIMITER + m.getGenre();
    }
}
