package tcp.movie_service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
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
}
