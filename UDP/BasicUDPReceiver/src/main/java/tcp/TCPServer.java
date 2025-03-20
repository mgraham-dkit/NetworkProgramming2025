package tcp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TCPServer {
    public static void main(String[] args) {
        try {
            ServerSocket connectionSocket = new ServerSocket(10080);

            while(true){
                Socket clientDataSocket = connectionSocket.accept();
                // Set up streams to communicate
                Scanner in = new Scanner(clientDataSocket.getInputStream());
                PrintWriter out = new PrintWriter(clientDataSocket.getOutputStream());

                // Receive a message
                String response = in.nextLine();
                System.out.println("Request: " + response);

                out.println("Request received!");
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
