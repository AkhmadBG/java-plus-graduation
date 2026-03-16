package ru.practicum.ewm.stats.collector.mapper;

import com.google.protobuf.Timestamp;
import org.mapstruct.Mapper;
import ru.practicum.ewm.stats.avro.ActionTypeAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.practicum.ewm.stats.proto.ActionTypeProto;
import ru.practicum.ewm.stats.proto.UserActionProto;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface UserActionMapper {

    UserActionAvro mapToUserActionAvro(UserActionProto userActionProto);

    default ActionTypeAvro toAvroActionType(ActionTypeProto actionTypeProto) {
        if (actionTypeProto == null) {
            throw new IllegalArgumentException("ActionTypeProto cannot be null.");
        }

        return switch (actionTypeProto) {
            case ACTION_VIEW -> ActionTypeAvro.VIEW;
            case ACTION_REGISTER -> ActionTypeAvro.REGISTER;
            case ACTION_LIKE -> ActionTypeAvro.LIKE;
            case UNRECOGNIZED ->
                    throw new IllegalArgumentException("Cannot map unknown or unrecognized ActionTypeProto: " + actionTypeProto);
        };
    }

    default Instant toInstant(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
    }

}