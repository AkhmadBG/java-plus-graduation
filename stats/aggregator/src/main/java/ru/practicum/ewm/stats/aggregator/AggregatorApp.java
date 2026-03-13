package ru.practicum.ewm.stats.aggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({
        "ru.practicum.ewm.stats.aggregator",
        "ru.practicum.ewm.stats.kafkamodule"
})
@SpringBootApplication
public class AggregatorApp {

    public static void main(String[] args) {
        SpringApplication.run(AggregatorApp.class, args);
    }

}