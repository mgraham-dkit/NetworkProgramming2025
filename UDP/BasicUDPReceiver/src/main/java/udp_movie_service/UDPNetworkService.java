package udp_movie_service;

import in_class_worked_example.NetworkService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.*;

@Slf4j
public class UDPNetworkService implements NetworkService {
    private DatagramSocket socket;
    private int myPort;

    private InetAddress destIP;
    private int destPort;

    // Provide constructor for clients to use
    // This allows the client to set their socket to send to a specific server
    public UDPNetworkService(int myPort, String destHost, int destPort) throws UnknownHostException {
        this.myPort = myPort;
        this.destPort = destPort;

        this.destIP = InetAddress.getByName(destHost);
    }

    // Create constructor for server to use (this will allow the socket to always message the requesting client)
    public UDPNetworkService(int myPort) {
        this.myPort = myPort;
    }

    public void connect() throws SocketException {
        socket = new DatagramSocket(myPort);
    }

    public void disconnect(){
        if(this.socket != null){
            this.socket.close();
        }
    }

    public void send(String message) throws IOException {
        // Convert the string message to a byte array
        byte[] payloadToBeSent = message.getBytes();
        // Package the byte array into a packet for transmission
        DatagramPacket sendingPacket = new DatagramPacket(payloadToBeSent, payloadToBeSent.length, destIP,
                destPort);
        // Send response packet
        socket.send(sendingPacket);
    }

    public String receive() throws IOException {
        byte[] payload = new byte[1024 * 64];
        DatagramPacket incomingPacket = new DatagramPacket(payload, payload.length);
        // RECEIVE AND RETURN FOR PROCESSING:
        socket.receive(incomingPacket);
        log.info("Request received from " + incomingPacket.getAddress()+ ":" + incomingPacket.getPort());
        String incomingMessage = new String(incomingPacket.getData(), 0, incomingPacket.getLength());

        this.destIP = incomingPacket.getAddress();
        this.destPort = incomingPacket.getPort();
        return incomingMessage;
    }
}
