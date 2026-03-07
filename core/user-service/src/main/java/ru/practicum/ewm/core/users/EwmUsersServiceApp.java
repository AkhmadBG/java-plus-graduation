package ru.practicum.ewm.core.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = {
        "ru.practicum.ewm.core.interaction"
})
@SpringBootApplication(scanBasePackages = {
        "ru.practicum.ewm.core.users",
        "ru.practicum.ewm.core.interaction"
})
public class EwmUsersServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(EwmUsersServiceApp.class, args);
    }

}