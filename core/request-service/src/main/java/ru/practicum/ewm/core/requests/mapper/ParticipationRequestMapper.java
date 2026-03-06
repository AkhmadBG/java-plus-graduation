package ru.practicum.ewm.core.requests.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.core.interaction.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.core.interaction.util.DateFormatter;
import ru.practicum.ewm.core.requests.entity.ParticipationRequest;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface ParticipationRequestMapper {

    @Mapping(target = "created", source = "created")
    ParticipationRequestDto toDto(ParticipationRequest request);

    default String map(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return DateFormatter.format(dateTime);
    }

}