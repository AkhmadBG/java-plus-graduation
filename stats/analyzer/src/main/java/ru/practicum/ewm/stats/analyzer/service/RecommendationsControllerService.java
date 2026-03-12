package ru.practicum.ewm.stats.analyzer.service;

import ru.practicum.ewm.stats.proto.InteractionsCountRequestProto;
import ru.practicum.ewm.stats.proto.RecommendedEventProto;
import ru.practicum.ewm.stats.proto.SimilarEventsRequestProto;
import ru.practicum.ewm.stats.proto.UserPredictionsRequestProto;

public interface RecommendationsControllerService {

    RecommendedEventProto getRecommendationsForUser(UserPredictionsRequestProto userPredictionsRequestProto);

    RecommendedEventProto getSimilarEvents(SimilarEventsRequestProto similarEventsRequestProto);

    RecommendedEventProto getInteractionsCount(InteractionsCountRequestProto interactionsCountRequestProto);

}
