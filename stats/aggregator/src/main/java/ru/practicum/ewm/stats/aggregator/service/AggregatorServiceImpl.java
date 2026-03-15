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

//        Матрица действий пользователей с мероприятиями eventId -> (userId -> weight)
//        Здесь хранится максимальный вес действия пользователя
//        с данным мероприятием.
    private final Map<Long, Map<Long, Double>> matrixOfUserActionWeights = new HashMap<>();
//        Общая сумма весов действий пользователей для каждого мероприятия eventId -> S_event
    private final Map<Long, Double> totalWeightsSums = new HashMap<>();
//        Сумма минимальных весов для каждой пары мероприятий eventA -> (eventB -> S_min)
//        S_min(A,B) = сумма min(weight_user_A, weight_user_B)
//        по всем пользователям
    private final Map<Long, Map<Long, Double>> minWeightsSums = new HashMap<>();

    @Override
    public void handleUserAction(UserActionAvro userActionAvro) {
        long eventId = userActionAvro.getEventId();
        long userId = userActionAvro.getUserId();
        String actionType = userActionAvro.getActionType()
                .name()
                .replace("ACTION_", "")
                .toLowerCase();
        System.out.println(actionType);
        System.out.println(userActionWeightsProperties);
//            Получаем вес действия из конфигурации.
        double newWeight = userActionWeightsProperties
                .getWeights()
                .get(actionType);
//            Получаем или создаём карту пользователей для мероприятия
        Map<Long, Double> users =
                matrixOfUserActionWeights.computeIfAbsent(eventId, e -> new HashMap<>());
//            Старый вес пользователя для мероприятия
        double oldWeight = users.getOrDefault(userId, 0.0);
//            Если новый вес не больше старого, то ничего пересчитывать не нужно
        if (newWeight <= oldWeight) {
            return;
        }
//            Обновляем матрицу действий
        users.put(userId, newWeight);
//            Разница между новым и старым весом используется для обновления сумм
        double deltaWeight = newWeight - oldWeight;
//            Обновляем сумму весов для мероприятия S_event = сумма всех весов пользователей
        totalWeightsSums.merge(eventId, deltaWeight, Double::sum);
//            Теперь нужно пересчитать сходство с другими мероприятиями
        for (Long otherEventId : matrixOfUserActionWeights.keySet()) {
//                Само с собой не считаем
            if (otherEventId.equals(eventId)) {
                continue;
            }

            Map<Long, Double> otherUsers = matrixOfUserActionWeights.get(otherEventId);
//                Проверяем взаимодействовал ли пользователь со вторым мероприятием
            Double weightB = otherUsers.get(userId);

            if (weightB == null) {
                continue;
            }
//                Старый вклад пользователя
            double oldMin = Math.min(oldWeight, weightB);
//                Новый вклад
            double newMin = Math.min(newWeight, weightB);
//                Насколько увеличилась сумма минимальных весов
            double deltaMin = newMin - oldMin;
//                Получаем текущую сумму S_min(A,B)
            double current = getMinWeight(eventId, otherEventId);
//                Обновляем её
            putMinWeight(eventId, otherEventId, current + deltaMin);
//                Теперь считаем cosine similarity
            double score = calculateSimilarity(eventId, otherEventId);
//                Отправляем результат в Kafka
            sendSimilarity(
                    eventId,
                    otherEventId,
                    score,
                    userActionAvro.getTimestamp()
            );
        }

    }

//        Сохраняет сумму минимальных весов для пары мероприятий
//        с упорядочиванием ID (чтобы избежать дубликатов)
    private void putMinWeight(long eventA, long eventB, double sum) {

        long first = Math.min(eventA, eventB);
        long second = Math.max(eventA, eventB);

        minWeightsSums
                .computeIfAbsent(first, e -> new HashMap<>())
                .put(second, sum);
    }

//        Получает сумму минимальных весов
    private double getMinWeight(long eventA, long eventB) {

        long first = Math.min(eventA, eventB);
        long second = Math.max(eventA, eventB);

        return minWeightsSums
                .computeIfAbsent(first, e -> new HashMap<>())
                .getOrDefault(second, 0.0);
    }

//        Вычисляет косинусное сходство мероприятий
    private double calculateSimilarity(long eventA, long eventB) {

        double sMin = getMinWeight(eventA, eventB);

        double totalWeightSumEventA = totalWeightsSums.getOrDefault(eventA, 0.0);
        double totalWeightSumEventB = totalWeightsSums.getOrDefault(eventB, 0.0);

        if (totalWeightSumEventA == 0 || totalWeightSumEventB == 0) {
            return 0;
        }

        return sMin / (Math.sqrt(totalWeightSumEventA) * Math.sqrt(totalWeightSumEventB));
    }

//        Отправка результата в Kafka
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