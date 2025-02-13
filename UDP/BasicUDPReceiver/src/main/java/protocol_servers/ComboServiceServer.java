package protocol_servers;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class ComboServiceServer {
    private int messageCount = 0;
    private final DateTimeFormatter dateTimeFormatter;

    public ComboServiceServer(){
        messageCount = 0;
        String dateTimePattern = "dd/MM/yyyy HH:mm:ss ";
        dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimePattern);
    }

    public void start(){
        int myPort = 12345;
        try (DatagramSocket mySocket = new DatagramSocket(myPort)) {
            log.info("Service now online...");
            boolean shutdown = false;
            while (!shutdown) {
                // RECEIVE MESSAGE:
                DatagramPacket incomingPacket = receiveRequest(mySocket);

                // EXTRACT REQUEST INFORMATION
                InetAddress clientIP = incomingPacket.getAddress();
                int clientPort = incomingPacket.getPort();
                String incomingMessage = new String(incomingPacket.getData(), 0, incomingPacket.getLength());

                // SERVICE LOGIC:
                messageCount++;

                String response = null;
                // Break request into components:
                String [] components = incomingMessage.split("%%");
                // Handle the specific requirements for this request
                switch(components[0].toUpperCase()){
                    case "ECHO" -> {
                        if(components.length == 2){
                            response = components[1];
                        }
                    }
                    case "DAYTIME" -> {
                        LocalDateTime timestamp = LocalDateTime.now();
                        response = timestamp.format(dateTimeFormatter);
                    }
                    case "STATS" -> {
                        response = String.valueOf(messageCount);
                        log.info("Stats requested. Current message count: " + messageCount);
                    }
                }

                // RESPONSE TRANSMISSION:
                // If a defined response was generated, send it
                if(response != null) {
                    sendResponse(response, clientIP, clientPort, mySocket);
                }
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

    private static void sendResponse(String response, InetAddress clientIP, int clientPort, DatagramSocket mySocket) throws IOException {
        // Convert the string message to a byte array
        byte[] payloadToBeSent = response.getBytes();
        // Package the byte array into a packet for transmission
        DatagramPacket sendingPacket = new DatagramPacket(payloadToBeSent, payloadToBeSent.length, clientIP,
                clientPort);
        // Send response packet
        mySocket.send(sendingPacket);
    }

    private DatagramPacket receiveRequest(DatagramSocket mySocket) throws IOException {
        byte[] payload = new byte[1024 * 64];
        DatagramPacket incomingPacket = new DatagramPacket(payload, payload.length);
        // RECEIVE AND RETURN FOR PROCESSING:
        mySocket.receive(incomingPacket);
        log.info("Request received from " + incomingPacket.getAddress()+ ":" + incomingPacket.getPort());

        return incomingPacket;
    }

    public static void main(String[] args) {
        // Create an instance of a server
        ComboServiceServer comboServer = new ComboServiceServer();
        // Start the service running
        comboServer.start();
    }
}
