package tcp.threading.movie_service.service;

import tcp.threading.movie_service.model.MovieManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadedMovieTCPServer {
    public static void main(String[] args) {
        try (ServerSocket connectionSocket = new ServerSocket(MovieUtilities.PORT)){

            MovieManager movieManager = new MovieManager();

            boolean validServerSession = true;
            while(validServerSession){
                Socket clientDataSocket = connectionSocket.accept();
                ServiceClientHandler clientHandler = new ServiceClientHandler(clientDataSocket, movieManager);
                Thread wrapper = new Thread(clientHandler);
                wrapper.start();
            }
        }catch (IOException e){
            System.out.println("Connection socket cannot be established");
        }
    }
}
