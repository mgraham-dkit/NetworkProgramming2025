package gui_based.layered_design.network;

import java.io.IOException;
import java.net.*;

public class NetworkService {
    private DatagramSocket socket;
    private InetAddress receiverAddress;
    private final InetAddress ipAddress;
    private int receiverPort;
    private final int senderPort = 5656;

    public NetworkService(InetAddress receiverAddress, int receiverPort) throws UnknownHostException {
        // Set up own information
        this.ipAddress = InetAddress.getByName("localhost");

        // Set up recipient information
        this.receiverAddress = receiverAddress;
        this.receiverPort = receiverPort;

    }

    public NetworkService() throws UnknownHostException {
        this(null, 0);
    }

    public InetAddress getReceiverAddress() {
        return receiverAddress;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public int getReceiverPort() {
        return receiverPort;
    }

    public int getSenderPort() {
        return senderPort;
    }

    public void setRecipient(String ip, int newReceiverPort) throws UnknownHostException{
        if(newReceiverPort < 1 || newReceiverPort > 65535){
            throw new NumberFormatException("Illegal port value supplied");
        }
        if(ip == null){
            throw new IllegalArgumentException("IP address to be messaged cannot be null");
        }

        this.receiverPort = newReceiverPort;
        this.receiverAddress = InetAddress.getByName(ip);
    }

    public void connect() throws UnknownHostException, SocketException {
        System.out.println("Setting up socket");
        // Create a socket with the hard-coded data
        socket = new DatagramSocket(this.senderPort, this.ipAddress);
        System.out.println("Socket established");
    }

    public void send(String message) throws IOException {
        if(socket == null){
            throw new SocketException("Socket is not connected");
        }
        byte [] payload = message.getBytes();
        DatagramPacket packet = new DatagramPacket(payload, payload.length, receiverAddress, receiverPort);
        socket.send(packet);
    }

    public String receive() throws IOException {
        if(socket == null){
            throw new SocketException("Socket is not connected");
        }
        byte [] responsePayload = new byte[1024*64];
        DatagramPacket reply = new DatagramPacket(responsePayload, responsePayload.length);
        socket.receive(reply);
        return new String(reply.getData(), 0, reply.getLength());
    }

    public void disconnect(){
        if(socket != null) {
            socket.close();
        }
    }
}
