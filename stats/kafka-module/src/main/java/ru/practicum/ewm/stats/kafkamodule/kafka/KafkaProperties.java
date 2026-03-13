package ru.practicum.ewm.stats.kafkamodule.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "kafka")
@Data
public class KafkaProperties {

    private Map<String, Map<String, String>> producers = new HashMap<>();
    private Map<String, Map<String, String>> consumers = new HashMap<>();

}