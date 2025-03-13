package in_class_worked_example;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class UDPComboServiceServer {
    private UDPNetworkService networkService;
    private int messageCount = 0;
    private final DateTimeFormatter dateTimeFormatter;

    public UDPComboServiceServer(UDPNetworkService networkService) {
        this.networkService = networkService;
        String dateTimePattern = "dd/MM/yyyy HH:mm:ss ";
        dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimePattern);
    }

    public static void main(String[] args) {
        UDPNetworkService networkService = new UDPNetworkService(ComboUtils.SERVER_PORT);
        UDPComboServiceServer server = new UDPComboServiceServer(networkService);
        server.run();
    }

    public void run() {
        log.info("Service now online...");

        boolean shutdown = false;
        try {
            networkService.connect();
            while (!shutdown) {

                // SERVICE LOGIC:
                messageCount++;
                try {
                    String request = networkService.receive();
                    String response = null;
                    // Break request into components:
                    String[] components = request.split(ComboUtils.DELIMITER);
                    // Handle the specific requirements for this request
                    switch (components[0]) {
                        case ComboUtils.ECHO:
                            if (components.length == 2) {
                                response = components[1];
                            }
                            break;
                        case ComboUtils.DAYTIME:
                            LocalDateTime timestamp = LocalDateTime.now();
                            response = timestamp.format(dateTimeFormatter);
                            break;
                        case ComboUtils.STATS:
                            response = String.valueOf(messageCount);
                            log.info("Stats requested. Current message count: " + messageCount);
                            break;
                    }

                    networkService.send(response);
                } catch (IOException e) {
                    networkService.disconnect();
                    networkService.connect();
                }
            }
        } catch (SocketException e) {
            log.error("Socket cannot bind on port " + ComboUtils.SERVER_PORT, e);
            System.out.println("Socket cannot bind. Program terminating...");
        }

    }
}
