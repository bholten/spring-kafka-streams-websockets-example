package com.github.bholten.kafka.example.websockets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

@Component
public class KafkaHandler implements WebSocketHandler {
    private static final Logger log = LoggerFactory.getLogger(KafkaHandler.class);
    private final DefaultDataBufferFactory dataBuffer = new DefaultDataBufferFactory();
    private final ReceiverOptions<String, Long> receiverOptions;

    public KafkaHandler(ReceiverOptions<String, Long> receiverOptions) {
        this.receiverOptions = receiverOptions;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session
                .send(consumeMessages())
                .doOnNext(message -> log.debug("Websocket message sent: {}", message))
                .doOnError(ex -> log.error("Error in the websocket handler: {}", ex.getMessage()))
                .log();
    }

    private Flux<WebSocketMessage> consumeMessages() {
        return KafkaReceiver
                .create(receiverOptions)
                .receive()
                .map(record -> record.key() + ": " + record.value().toString())
                .map(record -> new WebSocketMessage(WebSocketMessage.Type.TEXT, dataBuffer.wrap(record.getBytes())));
    }
}
