package ru.practicum.ewm.stats.analyzer.service;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.stats.analyzer.entity.Interaction;
import ru.practicum.ewm.stats.analyzer.entity.Similarity;
import ru.practicum.ewm.stats.analyzer.repository.InteractionRepository;
import ru.practicum.ewm.stats.analyzer.repository.SimilarityRepository;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.proto.InteractionsCountRequestProto;
import ru.practicum.ewm.stats.proto.RecommendedEventProto;
import ru.practicum.ewm.stats.proto.SimilarEventsRequestProto;
import ru.practicum.ewm.stats.proto.UserPredictionsRequestProto;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RecommendationsControllerServiceImpl implements RecommendationsControllerService {

    private final InteractionRepository interactionRepository;
    private final SimilarityRepository similarityRepository;

    @Override
    public Stream<RecommendedEventProto> getRecommendationsForUser(
            UserPredictionsRequestProto request) {

        long userId = request.getUserId();
        int limit = request.getMaxResults();

        List<Interaction> interactions = interactionRepository.findRecentInteractions(userId, limit);

        if (interactions.isEmpty()) {
            return Stream.empty();
        }

        Set<Long> viewedEvents = interactions.stream()
                .map(Interaction::getEventId)
                .collect(Collectors.toSet());

        return similarityRepository
                .findSimilarEventsForEvents(viewedEvents)
                .stream()
                .filter(s -> !viewedEvents.contains(s.getEvent2()))
                .sorted(Comparator.comparing(Similarity::getSimilarity).reversed())
                .limit(limit)
                .map(s -> {
                    double score = predictScore(userId, s.getEvent2());
                    return RecommendedEventProto.newBuilder()
                            .setEventId(s.getEvent2())
                            .setScore(score)
                            .build();
                });
    }

    @Override
    public Stream<RecommendedEventProto> getSimilarEvents(
            SimilarEventsRequestProto request) {

        long eventId = request.getEventId();
        long userId = request.getUserId();
        int limit = request.getMaxResults();

        List<Long> viewedEvents = interactionRepository.findEventsByUserId(userId);

        return similarityRepository
                .findSimilarEvents(eventId)
                .stream()
                .filter(s -> !viewedEvents.contains(
                        getOtherEvent(s, eventId)
                ))
                .sorted(Comparator.comparing(Similarity::getSimilarity).reversed())
                .limit(limit)
                .map(s -> RecommendedEventProto.newBuilder()
                        .setEventId(getOtherEvent(s, eventId))
                        .setScore(s.getSimilarity())
                        .build());
    }

    @Override
    public Stream<RecommendedEventProto> getInteractionsCount(InteractionsCountRequestProto request) {

        List<Long> eventIds = request.getEventIdList();

        return interactionRepository
                .sumRatingsByEventIds(eventIds)
                .stream()
                .map(r -> RecommendedEventProto.newBuilder()
                        .setEventId(r.getEventId())
                        .setScore(r.getRating())
                        .build());
    }

    private Long getOtherEvent(Similarity similarity, Long eventId) {
        if (similarity.getEvent1().equals(eventId)) {
            return similarity.getEvent2();
        } else {
            return similarity.getEvent1();
        }
    }

    private double predictScore(long userId, long targetEvent) {

        List<Interaction> userInteractions =
                interactionRepository.findByUserId(userId);

        double weightedSum = 0;
        double similaritySum = 0;

        for (Interaction interaction : userInteractions) {

            Long eventId = interaction.getEventId();

            Optional<Similarity> similarity =
                    similarityRepository.findSimilarity(eventId, targetEvent);

            if (similarity.isEmpty()) {
                continue;
            }

            double sim = similarity.get().getSimilarity();
            double rating = interaction.getRating();

            weightedSum += sim * rating;
            similaritySum += sim;
        }

        if (similaritySum == 0) {
            return 0;
        }

        return weightedSum / similaritySum;
    }

}