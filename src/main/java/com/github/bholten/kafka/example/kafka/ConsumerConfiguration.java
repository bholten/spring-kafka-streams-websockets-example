package com.github.bholten.kafka.example.kafka;

import com.github.bholten.kafka.example.properties.ApplicationProperties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;
import java.util.Properties;

@Configuration
public class ConsumerConfiguration {
    private static final Logger log = LoggerFactory.getLogger(ConsumerConfiguration.class);
    private final ApplicationProperties appProperties;

    public ConsumerConfiguration(ApplicationProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Bean
    public ReceiverOptions<String, Long> consumerProperties() {
        var props = new Properties() {{
            put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, appProperties.getConsumer().getAutoOffsetReset());
            put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, appProperties.getConsumer().getBootstrapServers());
            put(ConsumerConfig.GROUP_ID_CONFIG, appProperties.getConsumer().getGroupId());
            put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        }};

        return ReceiverOptions.<String, Long>create(props)
                .subscription(Collections.singleton(appProperties.getConsumer().getTopics()))
                .addAssignListener(receiverPartitions -> log.debug("Partitions assigned: {}", receiverPartitions))
                .addRevokeListener(receiverPartitions -> log.debug("Partitions revoked: {}", receiverPartitions));
    }
}
