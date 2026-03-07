package ru.practicum.ewm.core.events;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = {
        "ru.practicum.ewm.core.interaction"
})
@SpringBootApplication(scanBasePackages = {
        "ru.practicum.ewm.core.events",
        "ru.practicum.ewm.core.interaction"
})
public class EwmEventsServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(EwmEventsServiceApp.class, args);
    }

}