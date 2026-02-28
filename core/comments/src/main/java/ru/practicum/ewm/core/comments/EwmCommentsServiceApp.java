package ru.practicum.ewm.core.comments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = {
        "ru.practicum.ewm.core.interaction"
})
@SpringBootApplication(scanBasePackages = {
        "ru.practicum.ewm.core.comments",
        "ru.practicum.ewm.core.interaction"
})
public class EwmCommentsServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(EwmCommentsServiceApp.class, args);
    }

}
