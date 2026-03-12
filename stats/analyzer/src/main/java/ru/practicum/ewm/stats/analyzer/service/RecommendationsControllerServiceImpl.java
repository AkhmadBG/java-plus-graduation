package ru.practicum.ewm.stats.analyzer.service;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.stats.kafkamodule.kafka.KafkaClient;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.proto.InteractionsCountRequestProto;
import ru.practicum.ewm.stats.proto.RecommendedEventProto;
import ru.practicum.ewm.stats.proto.SimilarEventsRequestProto;
import ru.practicum.ewm.stats.proto.UserPredictionsRequestProto;

@Service
@RequiredArgsConstructor
public class RecommendationsControllerServiceImpl implements RecommendationsControllerService {

    private final KafkaClient kafkaClient;

    @Override
    public RecommendedEventProto getRecommendationsForUser(UserPredictionsRequestProto userPredictionsRequestProto) {
        return null;
    }

    @Override
    public RecommendedEventProto getSimilarEvents(SimilarEventsRequestProto similarEventsRequestProto) {
        return null;
    }

    @Override
    public RecommendedEventProto getInteractionsCount(InteractionsCountRequestProto interactionsCountRequestProto) {
        return null;
    }

}
