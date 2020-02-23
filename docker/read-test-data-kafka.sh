#!/bin/sh

# Opens a consumer in the terminal
# Watch it in another terminal window while you produce data
# Also note you can view the topics by going to localhost:3030
# This is a feature of the docker-compose file.
kafka-console-consumer \
    --bootstrap-server localhost:9092 \
    --topic word-count-output \
    --property print.key=true \
    --property print.value=true \
    --formatter kafka.tools.DefaultMessageFormatter \
    --key-deserializer org.apache.kafka.common.serialization.StringDeserializer \
    --value-deserializer org.apache.kafka.common.serialization.LongDeserializer \
    --from-beginning