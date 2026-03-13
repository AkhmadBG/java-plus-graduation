package ru.practicum.ewm.stats.aggregator.service;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.practicum.ewm.stats.kafkamodule.kafka.KafkaClient;
import ru.practicum.ewm.stats.kafkamodule.kafka.KafkaTopics;

@Service
@RequiredArgsConstructor
public class AggregatorServiceImpl implements AggregatorService {

    private final KafkaClient kafkaClient;

    @Override
    public void handleUserAction(UserActionAvro userActionAvro) {
        EventSimilarityAvro eventSimilarityAvro = EventSimilarityAvro.newBuilder().build();


        Producer<String, SpecificRecordBase> producer = kafkaClient.getProducer("aggregator");
        producer.send(new ProducerRecord<>(
                KafkaTopics.EVENT_SIMILARITY_TOPIC,
                String.valueOf(userActionAvro.getUserId()),
                eventSimilarityAvro
        ));
    }
}
