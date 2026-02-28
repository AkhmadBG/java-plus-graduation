package ru.practicum.ewm.core.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = {
        "ru.practicum.ewm.core.interaction"
})
@SpringBootApplication(scanBasePackages = {
        "ru.practicum.ewm.core.main",
        "ru.practicum.ewm.core.interaction"
})
public class EwmMainApp {

    public static void main(String[] args) {
        SpringApplication.run(EwmMainApp.class, args);
    }

}