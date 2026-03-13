package ru.practicum.ewm.stats.analyzer.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnalyzerRunner implements ApplicationRunner {

    private final AnalyzerConsumer consumer;

    @Override
    public void run(ApplicationArguments args) {
        consumer.start();
    }

}