package com.github.bholten.kafka.example.websockets;

import com.github.bholten.kafka.example.properties.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebSocketLifecycle {
    private static final Logger log = LoggerFactory.getLogger(WebSocketLifecycle.class);
    private final ApplicationProperties appProperties;
    private final WebSocketHandler webSocketHandler;

    public WebSocketLifecycle(ApplicationProperties appProperties, WebSocketHandler webSocketHandler) {
        this.appProperties = appProperties;
        this.webSocketHandler = webSocketHandler;
    }

    @Bean
    public HandlerMapping handlerMapping() {
        Map<String, WebSocketHandler> map = new HashMap<>() {{
            put(appProperties.getWebsocketPath(), webSocketHandler);
        }};

        HandlerMapping handlerMapping = new SimpleUrlHandlerMapping() {{
            setOrder(1);
            setUrlMap(map);
        }};

        log.info("WebSocket Handler mapping initiated: {}", map);
        return handlerMapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
