package com.github.bholten.kafka.example.tests

import com.github.bholten.kafka.example.config.KafkaStreamsConfig
import com.github.bholten.kafka.example.properties.ApplicationProperties
import com.github.bholten.kafka.example.streams.WordCountStream
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.TestInputTopic
import org.apache.kafka.streams.TestOutputTopic
import org.apache.kafka.streams.TopologyTestDriver
import spock.lang.Specification

class StreamsTopologyTest extends Specification {
    TopologyTestDriver testDriver
    TestInputTopic<String, String> inputTopic
    TestOutputTopic<String, Long> outputTopic

    def setup() {
        def appId = "test-app-${UUID.randomUUID().toString()}".toString()
        def streamsProperties = new KafkaStreamsConfig()
        streamsProperties.setApplicationId(appId)
        streamsProperties.setAutoOffsetReset("earliest")
        streamsProperties.setBootstrapServers("not-used")
        streamsProperties.setInputTopics("input-topic")
        streamsProperties.setOutputTopics("output-topic")

        def appProperties = new ApplicationProperties()
        appProperties.setStreams(streamsProperties)

        def props = new Properties()
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, appId)
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "not-used")
        props.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka-streams")
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().class)
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().class)

        def topology = new WordCountStream(appProperties, props)

        testDriver = new TopologyTestDriver(topology.createTopology(), props)
        inputTopic = testDriver.createInputTopic("input-topic", Serdes.String().serializer(), Serdes.String().serializer())
        outputTopic = testDriver.createOutputTopic("output-topic", Serdes.String().deserializer(), Serdes.Long().deserializer())
    }

    def cleanup() {
        testDriver.close()
    }

    def "it should count the words"() {
        when:
        ["test", "this is the test", "the quick brown fox"].forEach {
            inputTopic.pipeInput(UUID.randomUUID().toString(), it)
        }

        then:
        def results = outputTopic.readKeyValuesToMap()

        assert results.size() == 7
        assert results.get("test") == 2
        assert results.get("the") == 2
        assert results.get("is") == 1
        assert results.get("this") == 1
        assert results.get("quick") == 1
        assert results.get("brown") == 1
        assert results.get("fox") == 1
    }
}
