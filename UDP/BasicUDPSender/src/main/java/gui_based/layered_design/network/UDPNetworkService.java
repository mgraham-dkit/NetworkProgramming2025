package gui_based.layered_design.network;

import java.io.IOException;
import java.net.*;

public class UDPNetworkService {
    private DatagramSocket socket;
    private InetAddress destinationIP;
    private InetAddress senderIP;
    private int destPort;
    private int senderPort;

    public UDPNetworkService(String destIP, int destPort, int senderPort) throws UnknownHostException {
        this.destinationIP = InetAddress.getByName(destIP);
        this.senderIP = InetAddress.getByName("localhost");
        this.destPort = destPort;
        this.senderPort = senderPort;
    }

    public void connect() throws SocketException {
        if(socket != null && socket.isConnected()){
            throw new SocketException("Connection already established.");
        }
        socket = new DatagramSocket(senderPort);
        System.out.println("Socket connected");
    }

    public void disconnect(){
        if(socket != null && socket.isConnected()){
            socket.close();
        }
        System.out.println("Socket disconnected");
    }

    public void send(String message) throws IOException {
        if(socket == null){
            throw new SocketException("Socket is not connected");
        }
        byte [] payload = message.getBytes();
        DatagramPacket packet = new DatagramPacket(payload, payload.length, destinationIP, destPort);
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
}
