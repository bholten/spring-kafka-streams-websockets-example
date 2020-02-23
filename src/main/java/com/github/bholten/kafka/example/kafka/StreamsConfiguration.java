package com.github.bholten.kafka.example.kafka;

import com.github.bholten.kafka.example.properties.ApplicationProperties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class StreamsConfiguration {
    private final ApplicationProperties appProperties;

    public StreamsConfiguration(ApplicationProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Bean
    public Properties streamsProperties() {
        return new Properties() {{
           put(StreamsConfig.APPLICATION_ID_CONFIG, appProperties.getStreams().getApplicationId());
           put(StreamsConfig.consumerPrefix(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG),
                   appProperties.getStreams().getAutoOffsetReset());
           put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, appProperties.getStreams().getBootstrapServers());
           put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, appProperties.getStreams().getCommitIntervalMs());
           put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
           put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        }};
    }
}
