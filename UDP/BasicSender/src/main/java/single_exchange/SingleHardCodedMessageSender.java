package single_exchange;

import java.io.IOException;
import java.net.*;

public class SingleHardCodedMessageSender {
    public static void main(String[] args) {
        // SET-UP:
        // OUR address information - we listen for messages here
        int myPort = 5656;
        // Create a socket on which to listen for messages to that port
        try(DatagramSocket mySocket = new DatagramSocket(myPort)) {
            // Set timeout on the socket so it doesn't wait for a message for longer than 5000 milliseconds
            mySocket.setSoTimeout(5000);

            // Destination address information - IP and port
            InetAddress destinationIP = InetAddress.getByName("localhost");
            int destinationPort = 7777;

            // LOGIC:
            // Message to be sent
            String message = "Hello world!";

            // TRANSMISSION:
            // Condition the message for transmission
            byte[] payload = message.getBytes();
            // Build the packet to be sent
            DatagramPacket packet = new DatagramPacket(payload, payload.length, destinationIP, destinationPort);
            // Send message to server
            mySocket.send(packet);
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
}
