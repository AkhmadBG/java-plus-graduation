package ru.practicum.ewm.stats.aggregator.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AggregatorRunner implements ApplicationRunner {

    private final AggregatorConsumer consumer;

    @Override
    public void run(ApplicationArguments args) {
        consumer.start();
    }

}