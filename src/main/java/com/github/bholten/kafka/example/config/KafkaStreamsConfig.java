package com.github.bholten.kafka.example.config;

public class KafkaStreamsConfig {
    private String applicationId;
    private String autoOffsetReset;
    private String bootstrapServers;
    private String commitIntervalMs;
    private String inputTopics;
    private String outputTopics;

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getAutoOffsetReset() {
        return autoOffsetReset;
    }

    public void setAutoOffsetReset(String autoOffsetReset) {
        this.autoOffsetReset = autoOffsetReset;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getCommitIntervalMs() {
        return commitIntervalMs;
    }

    public void setCommitIntervalMs(String commitIntervalMs) {
        this.commitIntervalMs = commitIntervalMs;
    }

    public String getInputTopics() {
        return inputTopics;
    }

    public void setInputTopics(String inputTopics) {
        this.inputTopics = inputTopics;
    }

    public String getOutputTopics() {
        return outputTopics;
    }

    public void setOutputTopics(String outputTopics) {
        this.outputTopics = outputTopics;
    }
}
