package ru.practicum.ewm.stats.kafkamodule.kafka;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@RequiredArgsConstructor
public class KafkaClient {

    private final DefaultKafkaProducerFactory producerFactory;
    private final DefaultKafkaConsumerFactory consumerFactory;

    private final Map<String, Producer<String, SpecificRecordBase>> producers = new ConcurrentHashMap<>();

    private final List<Consumer<String, SpecificRecordBase>> consumers = new CopyOnWriteArrayList<>();

    public Producer<String, SpecificRecordBase> getProducer(String module) {
        return producers.computeIfAbsent(
                module,
                producerFactory::createProducer
        );
    }

    public Consumer<String, SpecificRecordBase> getConsumer(String module) {

        Consumer<String, SpecificRecordBase> consumer =
                consumerFactory.createConsumer(module);

        consumers.add(consumer);

        return consumer;
    }

    @PreDestroy
    public void stop() {

        producers.values().forEach(producer -> {
            try {
                producer.close();
            } catch (Exception ignored) {}
        });

        consumers.forEach(consumer -> {
            try {
                consumer.close();
            } catch (Exception ignored) {}
        });

    }

}