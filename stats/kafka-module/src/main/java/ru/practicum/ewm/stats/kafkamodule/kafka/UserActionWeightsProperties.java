package ru.practicum.ewm.stats.kafkamodule.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "kafka.action-weight")
public class UserActionWeightsProperties {

    private Map<String, Double> weights = new HashMap<>();

}
