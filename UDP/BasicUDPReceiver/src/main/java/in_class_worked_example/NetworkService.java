package in_class_worked_example;

import java.io.IOException;
import java.net.SocketException;

public interface NetworkService {
    void connect() throws SocketException;
    void disconnect();
    void send(String message) throws IOException;
    String receive() throws IOException;
}
