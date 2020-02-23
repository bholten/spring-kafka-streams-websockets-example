package com.github.bholten.kafka.example.tests

import com.github.bholten.kafka.example.Application
import com.github.bholten.kafka.example.tests.utils.TestConsumer
import com.github.bholten.kafka.example.tests.utils.TestProducer
import com.github.bholten.kafka.example.tests.utils.TestWebsocketClient
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import spock.lang.Specification

import java.time.Duration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [Application, TestConsumer, TestProducer, TestWebsocketClient] )
@EmbeddedKafka(topics = ['word-count-input', 'word-count-output'],
        partitions = 1,
        controlledShutdown = true,
        brokerProperties = ['transaction.state.log.min.isr=1',
                'transaction.state.log.replication.factor=1',
                'log.retention.ms=600000'])
@DirtiesContext
class IntegrationTests extends Specification {
    // the broker address of the Embedded Kafka server
    @Value('${spring.embedded.kafka.brokers}')
    String brokerAddress
    // The port of the Spring application
    @Value('${local.server.port}')
    int port
    // A Kafka Producer to send test data into the stream
    @Autowired
    KafkaProducer<String, String> testProducer
    // A Kafka Consumer to read/validate output data from the stream
    @Autowired
    KafkaConsumer<String, Long> testConsumer
    // The Embedded Kafka Broker object
    @Autowired
    static EmbeddedKafkaBroker embeddedKafkaBroker

    def setup() {
        testWebsocketClient.execute(port)
    }

    def setupSpec() {
        // To clear out the Kafka Streams state
        def tmpDir = new File('/tmp/kafka-streams')
        if (tmpDir.isDirectory()) {
            tmpDir.deleteDir()
        }
    }

    def cleanupSpec() {
        if (embeddedKafkaBroker) {
            embeddedKafkaBroker.destroy()
        }
    }

    def 'Spring Context loads'() {
        expect:
        true
    }

    def 'test word count'() {
        setup: 'Subscribe the websocket client and test Kafka consumer'
        testConsumer.subscribe(Collections.singleton('word-count-output'))

        when: 'A sentence is delivered to the input topic'
        def sentence = 'the quick brown fox jumps over the lazy dog and then jumps over the dog again'
        testProducer.send(new ProducerRecord<String, String>('word-count-input', 'test-key', sentence))

        and: 'The test consumer consumes it on the output topic'
        def records = testConsumer.poll(Duration.ofMillis(1000)).records('word-count-output')
        // Block for ~10x the poll duration so that the consume can complete
        Thread.sleep(10000)

        then: 'The output will match the expected value'
        def wordMap = [:]
        records.forEach { wordMap.put(it.key(), it.value()) }

        assert wordMap['quick'] == 1
        assert wordMap['brown'] == 1
        assert wordMap['fox'] == 1
        assert wordMap['lazy'] == 1
        assert wordMap['and'] == 1
        assert wordMap['then'] == 1
        assert wordMap['jumps'] == 2
        assert wordMap['over'] == 2
        assert wordMap['the'] == 3
        assert wordMap['dog'] == 2
        assert wordMap['again'] == 1

        // TODO: hookup a websocket client and verify the same information out of the websocket

        cleanup:
        testConsumer.unsubscribe()
    }
}
