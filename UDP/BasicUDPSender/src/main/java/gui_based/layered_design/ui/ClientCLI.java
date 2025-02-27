package gui_based.layered_design.ui;

import gui_based.layered_design.network.UDPNetworkService;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class ClientCLI {
    public static void main(String[] args) throws SocketException {
        // SET-UP:
        Scanner input = new Scanner(System.in);
        int myPort = 5656;
        String destHostname = "localhost";
        int destPort = 7777;
        UDPNetworkService networkService = createNetworkService(destHostname, destPort, myPort, input);

        networkService.connect();

        // LOGIC:
        System.out.println("Do you wish to send a message? (Y/y for yes, any other key for no)");
        String choice = input.nextLine();
        while (choice.equalsIgnoreCase("y")) {
            try {
                System.out.println("Please enter the message to be sent (SHUTDOWN to end the service):");
                String message = input.nextLine();

                // TRANSMISSION:
                networkService.send(message);

                // RECEIVE RESPONSE:
                String response = networkService.receive();

                // LOGIC STAGE (Part 2)
                System.out.println("Response received: " + response);

                if (!message.equalsIgnoreCase("SHUTDOWN")) {
                    System.out.println("Do you wish to send another message? (Y/y for yes, any other key for no)");
                    choice = input.nextLine();
                } else {
                    choice = "n";
                }
            } catch (UnknownHostException e) {
                System.out.println("IP address is not recognised");
                System.out.println(e.getMessage());
            } catch (SocketException e) {
                System.out.println("Problem occurred on the socket");
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println("Problem occurred when working with the socket");
                System.out.println(e.getMessage());
            }
        }


        System.out.println("Message transmitter terminating...");
    }

    private static UDPNetworkService createNetworkService(String destHostname, int destPort, int myPort, Scanner input) {
        UDPNetworkService networkLayer = null;
        boolean acceptedHost = false;
        while(!acceptedHost) {
            try {
                networkLayer = new UDPNetworkService(destHostname, destPort, myPort);
                acceptedHost = true;
            } catch (UnknownHostException e) {
                System.out.println("Host could not be found. Please enter a new host:");
                destHostname = input.nextLine();
            }
        }
        return networkLayer;
    }
}
