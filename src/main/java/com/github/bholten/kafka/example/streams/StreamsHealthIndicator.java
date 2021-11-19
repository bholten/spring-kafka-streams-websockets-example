package com.github.bholten.kafka.example.streams;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class StreamsHealthIndicator implements ReactiveHealthIndicator {
    private final StreamsLifecycle streamsLifecycle;

    public StreamsHealthIndicator(StreamsLifecycle streamsLifecycle) {
        this.streamsLifecycle = streamsLifecycle;
    }

    @Override
    public Mono<Health> health() {
        return Mono.just(streamsLifecycle.isHealthy() ? Health.up().build() : Health.down().build());
    }
}
