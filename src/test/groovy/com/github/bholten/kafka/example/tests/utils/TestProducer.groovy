package com.github.bholten.kafka.example.tests.utils

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestProducer {
    // the broker address of the Embedded Kafka server
    @Value('${spring.embedded.kafka.brokers}')
    String brokerAddress

    @Bean
    KafkaProducer<String, String> createTestProducer() {
        def testProducerProps = [:]
        testProducerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerAddress)
        testProducerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer)
        testProducerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer)

        new KafkaProducer<String, String>(testProducerProps)
    }
}
