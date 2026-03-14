package ru.practicum.ewm.stats.kafkamodule.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "kafka.topic")
public class KafkaTopics {

    private String userActions;
    private String eventSimilarity;

}