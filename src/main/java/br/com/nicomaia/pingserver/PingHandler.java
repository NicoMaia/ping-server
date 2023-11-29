package br.com.nicomaia.pingserver;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class PingHandler extends TextWebSocketHandler {
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException, InterruptedException {
        while (session.isOpen()) {
            InetAddress address = Inet4Address.getByName(message.getPayload());
            LocalDateTime started = LocalDateTime.now();

            if (address.isReachable(60000)) {
                session.sendMessage(new TextMessage(sendDiff(address, started)));
            } else {
                session.sendMessage(new TextMessage("Esgotado o tempo limite do pedido."));
            }

            Thread.sleep(1000);
        }
    }

    public String sendDiff(InetAddress address, LocalDateTime started) {
        LocalDateTime finished = LocalDateTime.now();

        return String.format("%s: tempo=%dms%n", address.getHostAddress(), ChronoUnit.MILLIS.between(started, finished));
    }
}