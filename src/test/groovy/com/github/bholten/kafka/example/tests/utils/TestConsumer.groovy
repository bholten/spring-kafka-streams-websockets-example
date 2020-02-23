package com.github.bholten.kafka.example.tests.utils

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.LongDeserializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestConsumer {
    // the broker address of the Embedded Kafka server
    @Value('${spring.embedded.kafka.brokers}')
    String brokerAddress

    @Bean
    KafkaConsumer<String, Long> createTestConsumer() {
        def testConsumerProps = [:]
        testConsumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerAddress)
        testConsumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, 'earliest')
        testConsumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, 'test-consumer-it')
        testConsumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer)
        testConsumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, LongDeserializer)
        testConsumerProps.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 1000)

        new KafkaConsumer<String, Long>(testConsumerProps)
    }
}
