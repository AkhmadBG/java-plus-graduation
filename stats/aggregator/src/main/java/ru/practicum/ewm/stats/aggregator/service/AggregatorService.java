package ru.practicum.ewm.stats.aggregator.service;

import ru.practicum.ewm.stats.avro.UserActionAvro;

public interface AggregatorService {

    void handleUserAction(UserActionAvro userActionAvro);

}
