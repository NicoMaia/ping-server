package br.com.nicomaia.pingserver.handlers;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final PingHandler pingHandler;

    public WebSocketConfig(PingHandler pingHandler) {
        this.pingHandler = pingHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(pingHandler, "/ping").setAllowedOrigins("*");
    }

}
