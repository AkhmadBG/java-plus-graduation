package ru.practicum.ewm.stats.kafkamodule.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;

@Component
@RequiredArgsConstructor
public class DefaultKafkaProducerFactory {

    private final KafkaProperties properties;

    public Producer<String, SpecificRecordBase> createProducer(String module) {

        Map<String, String> configMap = properties.getProducers().get(module);
        Map<String, String> commonProperties = properties.getCommonProperties();

        if (configMap == null) {
            throw new IllegalArgumentException("No kafka producer config for module: " + module);
        }

        Properties config = new Properties();
        config.putAll(configMap);
        config.putAll(commonProperties);

        return new KafkaProducer<>(config);
    }

}