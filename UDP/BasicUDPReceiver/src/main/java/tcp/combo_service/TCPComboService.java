package tcp.combo_service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class TCPComboService {
    public static void main(String[] args) {
        int messageCount = 0;
        String dateTimePattern = "dd/MM/yyyy HH:mm:ss ";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimePattern);
        try {
            ServerSocket connectionSocket = new ServerSocket(ComboUtilities.PORT);

            while(true){
                Socket clientDataSocket = connectionSocket.accept();
                // Set up streams to communicate
                Scanner in = new Scanner(clientDataSocket.getInputStream());
                PrintWriter out = new PrintWriter(clientDataSocket.getOutputStream());

                // Receive a message
                String request = in.nextLine();
                System.out.println("Request: " + request);

                // PROTOCOL LOGIC GOES HERE::
                String response = null;
                String [] components = request.split(ComboUtilities.DELIMITER);
                switch(components[0]) {
                    case ComboUtilities.ECHO:
                        if(components.length == 2) {
                            response = components[1];
                        }else{
                            response = ComboUtilities.INVALID;
                        }
                        break;
                    case ComboUtilities.DAYTIME:
                        response = dateTimeFormatter.format(LocalDateTime.now());
                        break;
                    case ComboUtilities.STATS:
                        response = String.valueOf(messageCount);
                        break;
                    case ComboUtilities.EXIT:
                    default:
                        response = ComboUtilities.INVALID;
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
