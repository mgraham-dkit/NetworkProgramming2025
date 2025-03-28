package single_exchange;

import java.io.IOException;
import java.net.*;

public class SingleMessageReceiver {
    public static void main(String[] args) {
        // Establish MY listening port
        int myPort = 7777;

        // Create a window for server to access network
        try (DatagramSocket mySocket = new DatagramSocket(myPort)) {
            // WHILE LOOP STARTS HERE
            // Array to store incoming message
            byte[] payload = new byte[1024 * 64];
            // Packet to store incoming message
            DatagramPacket incomingPacket = new DatagramPacket(payload, payload.length);
            // Try to receive the message
            System.out.println("Waiting for packet....");
            mySocket.receive(incomingPacket);
            System.out.println("Packet received!");

            // Extract sender information from received packet:
            InetAddress clientIP = incomingPacket.getAddress();
            int clientPort = incomingPacket.getPort();

            // Extract data from received packet:
            int len = incomingPacket.getLength();
            String incomingMessage = new String(payload, 0, len);

            // Display to screen:
            System.out.println(clientIP.getHostAddress()+ ":" + clientPort + " -> " + incomingMessage);
        } catch (BindException e) {
            System.out.println("Port already in use. Please supply a different port next time!");
        } catch (SocketException e) {
            System.out.println("An error occurred while setting up local socket.");
        } catch (IOException e) {
            System.out.println("Problem occurred when waiting to receive/receiving a message. Please try again later.");
        }

        System.out.println("Server shutting down...");
    }
}
