package ru.practicum.ewm.stats.collector.service;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import ru.practicum.ewm.stats.kafkamodule.kafka.KafkaClient;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.practicum.ewm.stats.collector.mapper.UserActionMapper;
import ru.practicum.ewm.stats.kafkamodule.kafka.KafkaTopics;
import ru.practicum.ewm.stats.proto.UserActionProto;

@Service
@RequiredArgsConstructor
public class UserActionServiceImpl implements UserActionService {

    private final UserActionMapper userActionMapper;
    private final KafkaClient kafkaClient;
    private final KafkaTopics kafkaTopics;

    @Override
    public void userActionHandle(UserActionProto userActionProto) {
        UserActionAvro userActionAvro = userActionMapper.mapToUserActionAvro(userActionProto);
        Producer<String, SpecificRecordBase> producer = kafkaClient.getProducer("collector");
        producer.send(new ProducerRecord<>(
                kafkaTopics.getUserActions(),
                String.valueOf(userActionAvro.getUserId()),
                userActionAvro
        ));
    }

}