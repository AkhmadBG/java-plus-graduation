package ru.practicum.ewm.stats.kafkamodule.kafka;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        KafkaProperties.class,
        KafkaTopics.class,
        UserActionWeightsProperties.class
})
public class KafkaModuleConfiguration {
}