package com.github.bholten.kafka.example.properties;

import com.github.bholten.kafka.example.config.KafkaConsumerConfig;
import com.github.bholten.kafka.example.config.KafkaStreamsConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(value = "app")
public class ApplicationProperties {
    private KafkaConsumerConfig consumer;
    private KafkaStreamsConfig streams;
    private String websocketPath;

    public KafkaConsumerConfig getConsumer() {
        return consumer;
    }

    public void setConsumer(KafkaConsumerConfig consumer) {
        this.consumer = consumer;
    }

    public KafkaStreamsConfig getStreams() {
        return streams;
    }

    public void setStreams(KafkaStreamsConfig streams) {
        this.streams = streams;
    }

    public String getWebsocketPath() {
        return websocketPath;
    }

    public void setWebsocketPath(String websocketPath) {
        this.websocketPath = websocketPath;
    }
}
