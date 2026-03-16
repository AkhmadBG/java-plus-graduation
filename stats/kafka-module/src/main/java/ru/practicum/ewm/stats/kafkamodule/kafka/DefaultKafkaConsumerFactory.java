package ru.practicum.ewm.stats.kafkamodule.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;

@Component
@RequiredArgsConstructor
public class DefaultKafkaConsumerFactory {

    private final KafkaProperties properties;

    public Consumer<String, SpecificRecordBase> createConsumer(String module) {

        Map<String, String> configMap = properties.getConsumers().get(module);
        Map<String, String> commonProperties = properties.getCommonProperties();
        Map<String, String> consumers = properties.getConsumers().get(module);

        if (configMap == null) {
            throw new IllegalArgumentException("No kafka consumer config for module: " + module);
        }

        Properties config = new Properties();
        config.putAll(configMap);
        config.putAll(commonProperties);
        config.putAll(consumers);

        return new KafkaConsumer<>(config);
    }

}