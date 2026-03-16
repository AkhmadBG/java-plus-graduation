package ru.practicum.ewm.stats.analyzer.service;

import ru.practicum.ewm.stats.proto.InteractionsCountRequestProto;
import ru.practicum.ewm.stats.proto.RecommendedEventProto;
import ru.practicum.ewm.stats.proto.SimilarEventsRequestProto;
import ru.practicum.ewm.stats.proto.UserPredictionsRequestProto;

import java.util.stream.Stream;

public interface RecommendationsControllerService {

    Stream<RecommendedEventProto> getRecommendationsForUser(UserPredictionsRequestProto userPredictionsRequestProto);

    Stream<RecommendedEventProto> getSimilarEvents(SimilarEventsRequestProto similarEventsRequestProto);

    Stream<RecommendedEventProto> getInteractionsCount(InteractionsCountRequestProto interactionsCountRequestProto);

}