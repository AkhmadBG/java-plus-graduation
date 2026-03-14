package ru.practicum.ewm.stats.aggregator.service;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.avro.ActionTypeAvro;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.practicum.ewm.stats.kafkamodule.kafka.KafkaClient;
import ru.practicum.ewm.stats.kafkamodule.kafka.KafkaTopics;
import ru.practicum.ewm.stats.kafkamodule.kafka.UserActionWeightsProperties;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AggregatorServiceImpl implements AggregatorService {

    private final KafkaClient kafkaClient;
    private final KafkaTopics kafkaTopics;
    private final UserActionWeightsProperties userActionWeightsProperties;


    Map<Long, Map<Long, Double>> minWeightsSums = new HashMap<>();
    //--Event-----Event-Weight--
    // пример сохранения суммы минимальных весов мероприятий A и B
    // minWeightsSum.computeIfAbsent(Long, new HashMap()).put(Long, Double);

    Map<Long, Map<Long, Double>> matrixOfUserActionWeights = new HashMap<>();
    //--Event-----User--Weight--

    @Override
    public void handleUserAction(UserActionAvro userActionAvro) {
        EventSimilarityAvro eventSimilarityAvro = EventSimilarityAvro.newBuilder().build();

        Long eventId = userActionAvro.getEventId();
        Long userId = userActionAvro.getUserId();
        ActionTypeAvro actionType = userActionAvro.getActionType();

        Double actionTypeWeight = userActionWeightsProperties.getWeights().get(actionType.name().toLowerCase());

        Map<Long, Double> userActionWeight = matrixOfUserActionWeights.get(eventId);
        if (userActionWeight != null) {
            Double weight = userActionWeight.get(userId);
            if (weight < actionTypeWeight) {
                matrixOfUserActionWeights.get(eventId).put(userId, actionTypeWeight);
            }
        } else {
            matrixOfUserActionWeights.get(eventId).put(userId, actionTypeWeight);
        }

        Double score = 0.0;



        eventSimilarityAvro.setEventA();
        eventSimilarityAvro.setEventB();
        eventSimilarityAvro.setScore();

        Map<Long, Double> eventMap = minWeightsSums.get(eventId);
        if (eventMap == null) {
            minWeightsSums.
        }

        eventSimilarityAvro.setTimestamp(userActionAvro.getTimestamp());

        Producer<String, SpecificRecordBase> producer = kafkaClient.getProducer("aggregator");
        producer.send(new ProducerRecord<>(
                kafkaTopics.getEventSimilarity(),
//                String.valueOf(userActionAvro.getUserId()),
                eventSimilarityAvro
        ));
    }

    public void put(long eventA, long eventB, double sum) {
        long first = Math.min(eventA, eventB);
        long second = Math.max(eventA, eventB);

        minWeightsSums
                .computeIfAbsent(first, e -> new HashMap<>())
                .put(second, sum);
    }

    public double get(long eventA, long eventB) {
        long first = Math.min(eventA, eventB);
        long second = Math.max(eventA, eventB);

        return minWeightsSums
                .computeIfAbsent(first, e -> new HashMap<>())
                .getOrDefault(second, 0.0);
    }

    private Double getScore() {

    }

}
