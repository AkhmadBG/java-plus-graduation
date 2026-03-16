package ru.practicum.ewm.stats.analyzer.service;

import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;

public interface AnalyzerService {

    void handleUserAction(UserActionAvro userActionAvro);

    void handleEventSimilarity(EventSimilarityAvro eventSimilarityAvro);

}