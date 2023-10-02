import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class PingController {
    private final Socket clientSocket;
    private final InputStream clientInputStream;
    private final OutputStream clientOutputStream;

    public PingController(Socket clientSocket, InputStream clientInputStream, OutputStream clientOutputStream) {
        this.clientSocket = clientSocket;
        this.clientInputStream = clientInputStream;
        this.clientOutputStream = clientOutputStream;
    }

    public void handle(PingRequest command) {
        try {
            setupCancel();
            sendCancelMessage();
            InetAddress address = Inet4Address.getByName(command.getAddress());

            while (!clientSocket.isClosed()) {
                LocalDateTime started = LocalDateTime.now();

                try {
                    if (address.isReachable(60000)) {
                        sendDiff(address, started);
                    }
                } catch (IllegalArgumentException e) {
                    sendDiff(address, started);
                }

                Thread.sleep(1000);
            }
        } catch (UnknownHostException e) {
            throw new BusinessException("Unable to find host", new RuntimeException(e));
        } catch (InterruptedException | IOException e) {
            throw new BusinessException("Unexpected error", new RuntimeException(e));
        }
    }

    private void setupCancel() throws IOException {
        new Thread(() -> {
            try {
                Scanner scanner = new Scanner(clientInputStream);
                scanner.hasNextLine();
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void sendDiff(InetAddress address, LocalDateTime started) {
        LocalDateTime finished = LocalDateTime.now();

        try {
            clientOutputStream.write(String.format("%s: tempo=%dms%n", address.getHostAddress(), ChronoUnit.MILLIS.between(started, finished)).getBytes());
            clientOutputStream.flush();
        } catch (IOException e) {
            throw new BusinessException("Unable to write response", new RuntimeException(e));
        }
    }

    public void sendCancelMessage() {
        try {
            clientOutputStream.write("Press enter to cancel...\n".getBytes());
            clientOutputStream.flush();
        } catch (IOException e) {
            throw new BusinessException("Unable to write response", new RuntimeException(e));
        }
    }
}
