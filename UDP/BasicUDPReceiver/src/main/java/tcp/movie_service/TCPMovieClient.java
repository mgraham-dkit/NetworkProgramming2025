package tcp.movie_service;

import tcp.combo_service.ComboUtilities;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TCPMovieClient {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        try {
            // Connect to server
            Socket dataSocket = new Socket(MovieUtilities.HOSTNAME, MovieUtilities.PORT);

            // Set up streams to communicate
            Scanner in = new Scanner(dataSocket.getInputStream());
            PrintWriter out = new PrintWriter(dataSocket.getOutputStream());

            System.out.println("Please enter the message to be sent:");
            String message = input.nextLine();

            // Send a message
            out.println(message);
            out.flush();

            // Receive a message
            String response = in.nextLine();
            System.out.println("Response: " + response);


            // Shut down communication
            out.close();
            in.close();

            // Shut down socket
            dataSocket.close();

        }catch(IOException e){
            System.out.println("Could not connect to server");
        }
    }
}
