package ru.practicum.ewm.core.main.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.practicum.ewm.core.interaction.dto.event.EventFullDto;
import ru.practicum.ewm.core.interaction.dto.event.EventShortDto;
import ru.practicum.ewm.core.interaction.dto.event.LocationDto;
import ru.practicum.ewm.core.interaction.dto.event.NewEventDto;
import ru.practicum.ewm.core.main.entity.Category;
import ru.practicum.ewm.core.main.entity.Event;
import ru.practicum.ewm.core.main.entity.User;
import ru.practicum.ewm.core.interaction.enums.EventState;
import ru.practicum.ewm.core.interaction.util.DateFormatter;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring",
        uses = {CategoryMapper.class, UserMapper.class, LocationMapper.class},
        imports = {LocalDateTime.class, EventState.class})
public interface EventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", expression = "java(LocalDateTime.now())")
    @Mapping(target = "state", constant = "PENDING")
    @Mapping(target = "confirmedRequests", constant = "0L")
    @Mapping(target = "views", constant = "0L")
    @Mapping(target = "paid", source = "newEventDto.paid", defaultExpression = "java(false)")
    @Mapping(target = "participantLimit", source = "newEventDto.participantLimit", defaultExpression = "java(0)")
    @Mapping(target = "requestModeration", source = "newEventDto.requestModeration", defaultExpression = "java(true)")
    @Mapping(target = "eventDate", expression = "java(parseDate(newEventDto.getEventDate()))")
    @Mapping(target = "location", source = "location")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Event toEvent(NewEventDto newEventDto, Category category, User initiator,
                  LocationDto location);

    @Mapping(target = "eventDate", expression = "java(formatDate(event.getEventDate()))")
    @Mapping(target = "createdOn", expression = "java(formatDate(event.getCreatedOn()))")
    @Mapping(target = "publishedOn", expression = "java(formatDate(event.getPublishedOn()))")
    @Mapping(target = "state", expression = "java(event.getState().name())")
    EventFullDto toEventFullDto(Event event);

    @Mapping(target = "eventDate", expression = "java(formatDate(event.getEventDate()))")
    EventShortDto toEventShortDto(Event event);

    default String formatDate(LocalDateTime dateTime) {
        return DateFormatter.format(dateTime);
    }

    default LocalDateTime parseDate(String dateString) {
        return DateFormatter.parse(dateString);
    }

    @AfterMapping
    default void setDefaultValues(@MappingTarget Event.EventBuilder eventBuilder, NewEventDto newEventDto) {
        if (newEventDto.getPaid() == null) {
            eventBuilder.paid(false);
        }
        if (newEventDto.getParticipantLimit() == null) {
            eventBuilder.participantLimit(0);
        }
        if (newEventDto.getRequestModeration() == null) {
            eventBuilder.requestModeration(true);
        }
    }
}