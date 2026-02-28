package ru.practicum.ewm.core.categories;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = {
        "ru.practicum.ewm.core.interaction"
})
@SpringBootApplication(scanBasePackages = {
        "ru.practicum.ewm.core.categories",
        "ru.practicum.ewm.core.interaction"
})
public class EwmCategoriesServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(EwmCategoriesServiceApp.class, args);
    }

}