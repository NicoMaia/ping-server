import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class FrontController {
    private final Socket clientSocket;
    private final InputStream clientInputStream;
    private final OutputStream clientOutputStream;

    public FrontController(Socket clientSocket, InputStream clientInputStream, OutputStream clientOutputStream) {
        this.clientSocket = clientSocket;
        this.clientInputStream = clientInputStream;
        this.clientOutputStream = clientOutputStream;
    }

    public void handle(String command) {
        if (command.startsWith("PING ")) {
            String address = command.substring(5);

            PingRequest pingRequest = new PingRequest(address);
            PingController pingController = new PingController(clientSocket, clientInputStream, clientOutputStream);

            pingController.handle(pingRequest);
        }
    }
}
