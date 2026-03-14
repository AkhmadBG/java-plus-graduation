package ru.practicum.ewm.stats.analyzer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.analyzer.entity.Interaction;
import ru.practicum.ewm.stats.analyzer.entity.Similarity;
import ru.practicum.ewm.stats.analyzer.repository.InteractionRepository;
import ru.practicum.ewm.stats.analyzer.repository.SimilarityRepository;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.practicum.ewm.stats.kafkamodule.kafka.UserActionWeightsProperties;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnalyzerServiceImpl implements AnalyzerService {

    private final InteractionRepository interactionRepository;
    private final SimilarityRepository similarityRepository;
    private final UserActionWeightsProperties userActionWeightsProperties;

    @Override
    public void handleUserAction(UserActionAvro userActionAvro) {
        Double rating = userActionWeightsProperties
                .getWeights()
                .get(userActionAvro.getActionType().name().toLowerCase());

        Interaction interaction = Interaction.builder()
                .userId(userActionAvro.getUserId())
                .eventId(userActionAvro.getEventId())
                .created(LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(userActionAvro.getTimestamp().toEpochMilli()),
                        ZoneId.systemDefault()
                ))
                .rating(rating)
                .build();

        Optional<Interaction> oldInteraction = interactionRepository
                .findByUserIdAndEventId(userActionAvro.getUserId(), userActionAvro.getEventId());

        if (oldInteraction.isPresent()) {
            if (oldInteraction.get().getRating() < interaction.getRating()) {
                oldInteraction.get().setRating(interaction.getRating());
                interactionRepository.save(oldInteraction.get());
            }
        } else {
            interactionRepository.save(interaction);
        }
    }

    @Override
    public void handleEventSimilarity(EventSimilarityAvro eventSimilarityAvro) {
        Similarity similarity = Similarity.builder()
                .event1(eventSimilarityAvro.getEventA())
                .event2(eventSimilarityAvro.getEventB())
                .similarity(eventSimilarityAvro.getScore())
                .created(LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(eventSimilarityAvro.getTimestamp().toEpochMilli()),
                        ZoneId.systemDefault()))
                .build();

        Optional<Similarity> oldSimilarity = similarityRepository
                .findByEvent1AndEvent2(eventSimilarityAvro.getEventA(), eventSimilarityAvro.getEventB());

        if (oldSimilarity.isPresent()) {
            oldSimilarity.get().setSimilarity(similarity.getSimilarity());
            similarityRepository.save(oldSimilarity.get());
        } else {
            similarityRepository.save(similarity);
        }
    }

}