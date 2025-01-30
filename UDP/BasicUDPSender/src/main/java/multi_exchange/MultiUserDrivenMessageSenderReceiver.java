package multi_exchange;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class MultiUserDrivenMessageSenderReceiver {
    public static void main(String[] args) {
        // SET-UP:
        Scanner input = new Scanner(System.in);

        int myPort = 5656;
        try(DatagramSocket mySocket = new DatagramSocket(myPort)) {
            mySocket.setSoTimeout(5000);

            InetAddress destinationIP = InetAddress.getByName("localhost");
            int destinationPort = 7777;

            // LOGIC:
            System.out.println("Do you wish to send a message? (Y/y for yes, any other key for no)");
            String choice = input.nextLine();
            while(choice.equalsIgnoreCase("y")) {
                System.out.println("Please enter the message to be sent (SHUTDOWN to end the service):");
                String message = input.nextLine();

                // TRANSMISSION:
                byte[] payload = message.getBytes();
                DatagramPacket packet = new DatagramPacket(payload, payload.length, destinationIP, destinationPort);
                mySocket.send(packet);

                // RECEIVE RESPONSE:
                byte[] receivedMessage = new byte[50];
                DatagramPacket incomingMessage = new DatagramPacket(receivedMessage, receivedMessage.length);
                mySocket.receive(incomingMessage);
                receivedMessage = incomingMessage.getData();

                // LOGIC STAGE (Part 2)
                System.out.println("Response received: " + new String(receivedMessage, 0, incomingMessage.getLength()));

                if (!message.equalsIgnoreCase("SHUTDOWN")){
                    System.out.println("Do you wish to send another message? (Y/y for yes, any other key for no)");
                    choice = input.nextLine();
                }else {
                    choice = "n";
                }
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
        System.out.println("Message transmitter terminating...");
    }
}

