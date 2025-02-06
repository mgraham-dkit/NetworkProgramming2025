package multi_exchange;

import model.User;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;

public class AuthenticationReceiverSender {
    private static final String SHUT_DOWN = "SHUTDOWN";
    private static final HashMap<String, User> users = new HashMap<>();

    public static void main(String[] args) {
        // SET UP:
        bootstrapUsers();

        int myPort = 7777;
        try (DatagramSocket mySocket = new DatagramSocket(myPort)) {
            boolean shutdown = false;
            while (!shutdown) {
                // SET UP FOR RECEIVE:
                byte[] payload = new byte[1024 * 64];
                DatagramPacket incomingPacket = new DatagramPacket(payload, payload.length);
                System.out.println("Waiting for packet....");

                // RECEIVE AND PROCESS:
                mySocket.receive(incomingPacket);
                InetAddress clientIP = incomingPacket.getAddress();
                int clientPort = incomingPacket.getPort();
                int len = incomingPacket.getLength();
                String incomingMessage = new String(payload, 0, len);

                // LOGIC BASED ON MESSAGE CONTENTS:
                System.out.println(clientIP.getHostAddress() + ":" + clientPort + " -> " + incomingMessage);

                String response;
                if (incomingMessage.equalsIgnoreCase(SHUT_DOWN)) {
                    shutdown = true;
                    response = "Service shutting down.";
                } else {
                    String[] components = incomingMessage.split("%%");
                    if (components.length == 2) {
                        // michelle%%password
                        // ["michelle", "password"]
                        String username = components[0];
                        String password = components[1];
                        User u = users.get(username);
                        if (u != null) {
                            if (password.equals(u.getPassword())) {
                                response = "SUCCESSFUL";
                            } else {
                                response = "FAILED";
                            }
                        } else {
                            response = "FAILED";
                        }
                    } else {
                        response = "MALFORMED_REQUEST";
                    }
                }


                // RESPONSE TRANSMISSION:
                byte[] payloadToBeSent = response.getBytes();
                DatagramPacket sendingPacket = new DatagramPacket(payloadToBeSent, payloadToBeSent.length, clientIP,
                        clientPort);
                mySocket.send(sendingPacket);
                System.out.println("Response sent.");
            }
        } catch (BindException e) {
            System.out.println("Port already in use. Please supply a different port next time!");
        } catch (SocketException e) {
            System.out.println("An error occurred while setting up local socket.");
        } catch (IOException e) {
            System.out.println("Problem occurred when waiting to receive/receiving a message. Please try again later.");
        }

        System.out.println("Service shutting down...");
    }

    private static void bootstrapUsers() {
        for (int i = 0; i < 5; i++) {
            String username = "user" + i;
            String password = "password" + i;
            users.put(username, new User(username, password));
        }
    }
}
