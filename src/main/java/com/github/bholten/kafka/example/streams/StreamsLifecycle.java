package com.github.bholten.kafka.example.streams;

import org.apache.kafka.streams.KafkaStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class StreamsLifecycle {
    private static final Logger log = LoggerFactory.getLogger(StreamsLifecycle.class);
    private final WordCountStream wordCountStream;
    private KafkaStreams streams;

    public StreamsLifecycle(WordCountStream wordCountStream) {
        this.wordCountStream = wordCountStream;
    }

    @PostConstruct
    public void start() {
        log.info("Starting the WordCount stream");
        streams = wordCountStream.createStream();
        streams.start();
    }

    @PreDestroy
    public void close() {
        if (streams != null) {
            log.warn("Closing the WordCount stream");
            streams.close();
        }
    }
}
