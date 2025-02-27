package gui_based.layered_design.network;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

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
}
