package udp_movie_service;


import in_class_worked_example.ComboUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.SocketException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
public class UDPMovieServer {
    private UDPNetworkService networkService;
    private MovieManager movieManager;

    public UDPMovieServer(UDPNetworkService networkService, MovieManager movieManager) {
        this.networkService = networkService;
        this.movieManager = movieManager;
    }

    public static void main(String[] args) {
        UDPNetworkService networkService = new UDPNetworkService(MovieUtilities.PORT);
        MovieManager movieManager = new MovieManager();
        UDPMovieServer server = new UDPMovieServer(networkService, movieManager);
        server.run();
    }

    public void run(){
        log.info("Service now online...");

        boolean shutdown = false;
        try {
            networkService.connect();
            while (!shutdown) {

                // SERVICE LOGIC:
                try {
                    String request = networkService.receive();
                    String response = null;
                    // Break request into components:
                    String[] components = request.split(MovieUtilities.DELIMITER);
                    // Handle the specific requirements for this request
                    switch (components[0]) {
                        case tcp.movie_service.MovieUtilities.ADD:
                            if(components.length == 4) {
                                try {
                                    String name = components[1];

                                    int year = Integer.parseInt(components[2]);
                                    if(year > LocalDate.now().getYear()){
                                        response = tcp.movie_service.MovieUtilities.INVALID_YEAR;
                                        break;
                                    }

                                    String genre = components[3];

                                    movieManager.add(name, year, genre);
                                    response = tcp.movie_service.MovieUtilities.ADDED;
                                }catch(NumberFormatException e){
                                    response = tcp.movie_service.MovieUtilities.NON_NUMERIC;
                                }
                            }
                            break;
                    }
                    networkService.send(response);
                } catch (IOException e) {
                    networkService.disconnect();
                    networkService.connect();
                }
            }
        } catch (SocketException e) {
            log.error("Socket cannot bind on port " + ComboUtils.SERVER_PORT, e);
            System.out.println("Socket cannot bind. Program terminating...");
        }
    }
}
