package ru.practicum.ewm.stats.aggregator.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.practicum.ewm.stats.kafkamodule.kafka.KafkaClient;
import ru.practicum.ewm.stats.kafkamodule.kafka.KafkaTopics;
import ru.practicum.ewm.stats.kafkamodule.kafka.UserActionWeightsProperties;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AggregatorServiceImpl implements AggregatorService {

    private final KafkaClient kafkaClient;
    private final KafkaTopics kafkaTopics;
    private final UserActionWeightsProperties userActionWeightsProperties;

    private final Map<Long, Map<Long, Double>> matrixOfUserActionWeights = new HashMap<>();
    private final Map<Long, Double> totalWeightsSums = new HashMap<>();
    private final Map<Long, Map<Long, Double>> minWeightsSums = new HashMap<>();

    @Override
    public void handleUserAction(UserActionAvro userActionAvro) {
        long eventId = userActionAvro.getEventId();
        long userId = userActionAvro.getUserId();
        String actionType = userActionAvro.getActionType()
                .name()
                .replace("ACTION_", "")
                .toLowerCase();
        double newWeight = userActionWeightsProperties
                .getWeights()
                .get(actionType);
        Map<Long, Double> users =
                matrixOfUserActionWeights.computeIfAbsent(eventId, e -> new HashMap<>());
        double oldWeight = users.getOrDefault(userId, 0.0);
        if (newWeight <= oldWeight) {
            return;
        }
        users.put(userId, newWeight);
        double deltaWeight = newWeight - oldWeight;
        totalWeightsSums.merge(eventId, deltaWeight, Double::sum);
        for (Long otherEventId : matrixOfUserActionWeights.keySet()) {
            if (otherEventId.equals(eventId)) {
                continue;
            }

            Map<Long, Double> otherUsers = matrixOfUserActionWeights.get(otherEventId);
            Double weightB = otherUsers.get(userId);

            if (weightB == null) {
                continue;
            }
            double oldMin = Math.min(oldWeight, weightB);
            double newMin = Math.min(newWeight, weightB);
            double deltaMin = newMin - oldMin;
            double current = getMinWeight(eventId, otherEventId);
            putMinWeight(eventId, otherEventId, current + deltaMin);
            double score = calculateSimilarity(eventId, otherEventId);
            sendSimilarity(
                    eventId,
                    otherEventId,
                    score,
                    userActionAvro.getTimestamp()
            );
        }

    }

    private void putMinWeight(long eventA, long eventB, double sum) {

        long first = Math.min(eventA, eventB);
        long second = Math.max(eventA, eventB);

        minWeightsSums
                .computeIfAbsent(first, e -> new HashMap<>())
                .put(second, sum);
    }

    private double getMinWeight(long eventA, long eventB) {

        long first = Math.min(eventA, eventB);
        long second = Math.max(eventA, eventB);

        return minWeightsSums
                .computeIfAbsent(first, e -> new HashMap<>())
                .getOrDefault(second, 0.0);
    }

    private double calculateSimilarity(long eventA, long eventB) {

        double sMin = getMinWeight(eventA, eventB);
        double totalWeightSumEventA = totalWeightsSums.getOrDefault(eventA, 0.0);
        double totalWeightSumEventB = totalWeightsSums.getOrDefault(eventB, 0.0);

        if (totalWeightSumEventA == 0 || totalWeightSumEventB == 0) {
            return 0;
        }

        return sMin / (Math.sqrt(totalWeightSumEventA) * Math.sqrt(totalWeightSumEventB));
    }

    private void sendSimilarity(long eventA, long eventB, double score, Instant timestamp) {

        long first = Math.min(eventA, eventB);
        long second = Math.max(eventA, eventB);

        EventSimilarityAvro eventSimilarityAvro = EventSimilarityAvro.newBuilder()
                .setEventA(first)
                .setEventB(second)
                .setScore(score)
                .setTimestamp(timestamp)
                .build();

        kafkaClient
                .getProducer("aggregator")
                .send(new ProducerRecord<>(
                        kafkaTopics.getEventSimilarity(),
                        eventSimilarityAvro
                ));
    }

}