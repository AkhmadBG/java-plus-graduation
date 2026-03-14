package ru.practicum.ewm.stats.analyzer.configuration;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.analyzer.service.AnalyzerService;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.practicum.ewm.stats.kafkamodule.kafka.KafkaClient;
import ru.practicum.ewm.stats.kafkamodule.kafka.KafkaTopics;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyzerConsumer {

    private final KafkaClient kafkaClient;
    private final KafkaTopics kafkaTopics;
    private final AnalyzerService analyzerService;

    public void start() {
        new Thread(this::userActionsConsumer).start();
        new Thread(this::eventSimilarityConsumer).start();
    }

    private void userActionsConsumer() {

        Consumer<String, SpecificRecordBase> consumer = kafkaClient.getConsumer("analyzer-user-actions");
        consumer.subscribe(List.of(kafkaTopics.getUserActions()));

        while (true) {

            ConsumerRecords<String, SpecificRecordBase> records = consumer.poll(Duration.ofMillis(1000));

            for (ConsumerRecord<String, SpecificRecordBase> record : records) {

                SpecificRecordBase value = record.value();

                if (value instanceof UserActionAvro userActionAvro) {
                    analyzerService.handleUserAction(userActionAvro);
                }

            }

        }

    }

    private void eventSimilarityConsumer() {

        Consumer<String, SpecificRecordBase> consumer = kafkaClient.getConsumer("analyzer-similarity");
        consumer.subscribe(List.of(kafkaTopics.getEventSimilarity()));

        while (true) {

            ConsumerRecords<String, SpecificRecordBase> records =
                    consumer.poll(Duration.ofMillis(1000));

            for (ConsumerRecord<String, SpecificRecordBase> record : records) {

                SpecificRecordBase value = record.value();

                if (value instanceof EventSimilarityAvro eventSimilarityAvro) {
                    analyzerService.handleEventSimilarity(eventSimilarityAvro);
                }

            }

        }

    }

}