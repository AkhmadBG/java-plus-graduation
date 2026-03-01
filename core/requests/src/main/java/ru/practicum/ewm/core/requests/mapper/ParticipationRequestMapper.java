package ru.practicum.ewm.core.requests.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.core.interaction.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.core.interaction.util.DateFormatter;
import ru.practicum.ewm.core.requests.entity.ParticipationRequest;

@Mapper(componentModel = "spring")
public interface ParticipationRequestMapper {

    @Mapping(target = "created", expression = "java(formatDate(request.getCreated()))")
    @Mapping(target = "requester", source = "requester")
    ParticipationRequestDto toDto(ParticipationRequest request);

    default String formatDate(java.time.LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return DateFormatter.format(dateTime);
    }

}