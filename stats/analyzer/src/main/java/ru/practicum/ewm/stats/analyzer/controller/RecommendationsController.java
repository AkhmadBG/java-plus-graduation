package ru.practicum.ewm.stats.analyzer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.analyzer.service.RecommendationsControllerService;
import ru.practicum.ewm.stats.proto.RecommendationsControllerGrpc;

@Service
@RequiredArgsConstructor
public class RecommendationsController extends RecommendationsControllerGrpc.RecommendationsControllerImplBase {

    private final RecommendationsControllerService recommendationsControllerService;

}
