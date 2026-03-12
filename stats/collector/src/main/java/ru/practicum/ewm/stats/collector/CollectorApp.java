package ru.practicum.ewm.stats.collector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({
        "ru.practicum.ewm.stats.collector",
        "ru.practicum.ewm.stats.kafkamodule"
})
@SpringBootApplication()
public class CollectorApp {

    public static void main(String[] args) {
        SpringApplication.run(CollectorApp.class, args);
    }

}