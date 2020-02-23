#!/bin/sh

# For local testing
# Topics must be created prior else Kafka Streams will crash
# This will create the topics after running the docker-compose file
kafka-topics --bootstrap-server localhost:9092 --topic word-count-input --create --partitions 1 --replication-factor 1
kafka-topics --bootstrap-server localhost:9092 --topic word-count-output --create --partitions 1 --replication-factor 1
