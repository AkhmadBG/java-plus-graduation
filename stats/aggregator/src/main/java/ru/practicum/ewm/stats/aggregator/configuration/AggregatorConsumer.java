package ru.practicum.ewm.stats.aggregator.configuration;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.aggregator.service.AggregatorService;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.practicum.ewm.stats.kafkamodule.kafka.KafkaClient;
import ru.practicum.ewm.stats.kafkamodule.kafka.KafkaTopics;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AggregatorConsumer {

    private final KafkaClient kafkaClient;
    private final KafkaTopics kafkaTopics;
    private final AggregatorService aggregatorService;

    public void start() {
        new Thread(this::consume).start();
    }

    private void consume() {

        Consumer<String, SpecificRecordBase> consumer = kafkaClient.getConsumer("aggregator");

        consumer.subscribe(List.of(
                kafkaTopics.getUserActions(),
                kafkaTopics.getEventSimilarity()
        ));

        while (true) {

            ConsumerRecords<String, SpecificRecordBase> records =
                    consumer.poll(Duration.ofMillis(1000));

            for (ConsumerRecord<String, SpecificRecordBase> record : records) {

                SpecificRecordBase value = record.value();

                if (value instanceof UserActionAvro userActionAvro) {
                    aggregatorService.handleUserAction(userActionAvro);
                }

            }

        }

    }

}