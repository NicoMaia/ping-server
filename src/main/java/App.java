import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

import static java.util.List.of;

public class App {
    List<String> ips = of(
            "192.168.0.1",
            "192.168.1.1",
            "192.168.1.3",
            "192.168.1.30",
            "192.168.1.31",
            "192.168.1.32",
            "192.168.1.160",
            "192.168.1.161",
            "192.168.100.1"
    );

    public static void main(String[] args) {
        new App().run();
    }

    private void run() {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(8080);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                forkConnection(clientSocket);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void forkConnection(Socket clientSocket) {
        System.out.println("New connection received from " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort() + "...");
        new Thread(() -> handleConnection(clientSocket)).start();
    }

    private static void handleConnection(Socket clientSocket) {
        try {
            InputStream clientInputStream = clientSocket.getInputStream();
            OutputStream clientOutputStream = clientSocket.getOutputStream();

            Scanner scanner = new Scanner(clientInputStream, StandardCharsets.UTF_8);

            while (scanner.hasNextLine()) {
                String command = scanner.nextLine();

                FrontController frontController = new FrontController(clientSocket, clientInputStream, clientOutputStream);
                frontController.handle(command);
            }
        } catch (IOException e) {
            throw new BusinessException("unable to contact client", new RuntimeException(e));
        }
    }
}
