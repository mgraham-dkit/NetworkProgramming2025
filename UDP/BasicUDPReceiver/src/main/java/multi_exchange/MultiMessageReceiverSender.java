package multi_exchange;

import java.io.IOException;
import java.net.*;

public class MultiMessageReceiverSender {
    private static final String SHUT_DOWN = "SHUTDOWN";

    public static void main(String[] args) {
        // SET UP:
        int myPort = 7777;
        try (DatagramSocket mySocket = new DatagramSocket(myPort)) {
            boolean shutdown = false;
            while(!shutdown){
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
                System.out.println(clientIP.getHostAddress()+ ":" + clientPort + " -> " + incomingMessage);

                if(incomingMessage.equalsIgnoreCase(SHUT_DOWN)){
                    shutdown = true;
                }
                // TRANSMISSION:
                // Create a reply message - if shutdown was received the message transmitted should indicate the
                // service will terminate.
                String response = null;
                if(shutdown){
                    response = "Service shutting down.";
                }else{
                    response = "Message received.";
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
}
