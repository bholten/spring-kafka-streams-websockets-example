package com.github.bholten.kafka.example.tests.utils

import com.github.bholten.kafka.example.properties.ApplicationProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import reactor.core.publisher.Mono

import java.time.Duration

@Component
class TestWebsocketClient {
    private static final Logger log = LoggerFactory.getLogger(TestWebsocketClient)
    @Autowired
    ApplicationProperties appProperties
    // HashMap to save the data encountered in the websocket
    HashMap<String, String> wordMap = [:]

    Mono<Void> execute(int port) {
        log.info("Creating websocket client on port: ${port}")
        def client = new ReactorNettyWebSocketClient()
        def uri = new URI("ws://localhost:${port}${appProperties.websocketPath}")

        client.execute(uri, new WebSocketHandler() {
            @Override
            Mono<Void> handle(WebSocketSession session) {
                session.receive()
                        .doOnNext { message ->
                            def text = message.getPayloadAsText().split(/: /)
                            wordMap.put(text[0], text[1])
                        }
                        .then()
            }
        })
    }
}
