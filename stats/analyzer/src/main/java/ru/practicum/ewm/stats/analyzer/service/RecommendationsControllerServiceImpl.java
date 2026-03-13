package ru.practicum.ewm.stats.analyzer.service;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.stats.analyzer.repository.InteractionRepository;
import ru.practicum.ewm.stats.analyzer.repository.SimilarityRepository;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.proto.InteractionsCountRequestProto;
import ru.practicum.ewm.stats.proto.RecommendedEventProto;
import ru.practicum.ewm.stats.proto.SimilarEventsRequestProto;
import ru.practicum.ewm.stats.proto.UserPredictionsRequestProto;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RecommendationsControllerServiceImpl implements RecommendationsControllerService {

    private final InteractionRepository interactionRepository;
    private final SimilarityRepository SimilarityRepository;
    private final AnalyzerService analyzerService;

    @Override
    public Stream<RecommendedEventProto> getRecommendationsForUser(UserPredictionsRequestProto request) {
        return null;
    }

    @Override
    public Stream<RecommendedEventProto> getSimilarEvents(SimilarEventsRequestProto similarEventsRequestProto) {
        return null;
    }

    @Override
    public Stream<RecommendedEventProto> getInteractionsCount(InteractionsCountRequestProto interactionsCountRequestProto) {
        return null;
    }

}
