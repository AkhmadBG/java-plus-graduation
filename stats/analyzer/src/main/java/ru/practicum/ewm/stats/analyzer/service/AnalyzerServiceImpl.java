package ru.practicum.ewm.stats.analyzer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.analyzer.repository.InteractionRepository;
import ru.practicum.ewm.stats.analyzer.repository.SimilarityRepository;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;

@Service
@RequiredArgsConstructor
public class AnalyzerServiceImpl implements AnalyzerService {

    private final InteractionRepository interactionRepository;
    private final SimilarityRepository SimilarityRepository;

    @Override
    public void handleUserAction(UserActionAvro userActionAvro) {
    }

    @Override
    public void handleEventSimilarity(EventSimilarityAvro eventSimilarityAvro) {
    }

}