package com.github.bholten.kafka.example.streams;

import com.github.bholten.kafka.example.properties.ApplicationProperties;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Properties;

@Component
public class WordCountStream {
    private final ApplicationProperties appProperties;
    private final Properties streamsProperties;

    public WordCountStream(ApplicationProperties appProperties,
                           Properties streamsProperties) {
        this.appProperties = appProperties;
        this.streamsProperties = streamsProperties;
    }

    public KafkaStreams createStream() {
        final Serde<String> keySerde = Serdes.String();
        final Serde<Long> valueSerde = Serdes.Long();

        StreamsBuilder builder = new StreamsBuilder();

        // Stream in text lines
        KStream<String, String> textLines = builder.stream(appProperties.getStreams().getInputTopics());
        // Count the words
        KTable<String, Long> wordCounts = textLines
                // lowercase
                .mapValues(line -> line.toLowerCase())
                // split the words up
                .flatMapValues(line -> Arrays.asList(line.split("\\W+")))
                // select new key
                .selectKey((oldKey, word) -> word)
                .groupByKey()
                // count the words up, store in RocksDB or in-memory
                .count(Materialized.as("counts-store"));

        // Write the changelog of the KTable back to kafka
        wordCounts.toStream().to(appProperties.getStreams().getOutputTopics(),
                Produced.with(keySerde, valueSerde));

        return new KafkaStreams(builder.build(), streamsProperties);
    }
}
