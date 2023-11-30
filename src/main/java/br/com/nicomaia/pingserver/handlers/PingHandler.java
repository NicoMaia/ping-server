package br.com.nicomaia.pingserver.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private static final int PING_TIMEOUT = 10000;

    private ObjectMapper mapper;

    public PingHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException, InterruptedException {
        InetAddress address = Inet4Address.getByName(message.getPayload());

        while (session.isOpen()) {
            session.sendMessage(getTextResponse(ping(address)));
            Thread.sleep(1000);
        }
    }

    private PingResponse ping(InetAddress address) throws IOException {
        LocalDateTime started = LocalDateTime.now();

        if (address.isReachable(PING_TIMEOUT)) {
            return sendSuccess(address, started);
        }

        return sendTimeout(address);
    }

    private TextMessage getTextResponse(PingResponse pingResponse) throws JsonProcessingException {
        return new TextMessage(mapper.writeValueAsString(pingResponse));
    }

    private PingResponse sendSuccess(InetAddress address, LocalDateTime started) {
        LocalDateTime finished = LocalDateTime.now();

        return PingResponse.success(
                address.getHostAddress(),
                ChronoUnit.MILLIS.between(started, finished));
    }

    private PingResponse sendTimeout(InetAddress address) {
        return PingResponse.timeout(address.getHostAddress());
    }
}
