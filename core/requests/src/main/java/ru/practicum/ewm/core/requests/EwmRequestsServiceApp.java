package ru.practicum.ewm.core.requests;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = {
        "ru.practicum.ewm.core.interaction"
})
@SpringBootApplication(scanBasePackages = {
        "ru.practicum.ewm.core.requests",
        "ru.practicum.ewm.core.interaction"
})
public class EwmRequestsServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(EwmRequestsServiceApp.class, args);
    }

}