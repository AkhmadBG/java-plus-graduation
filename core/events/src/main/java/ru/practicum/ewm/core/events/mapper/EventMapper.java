package ru.practicum.ewm.core.events.mapper;

import org.mapstruct.*;
import ru.practicum.ewm.core.interaction.dto.event.EventFullDto;
import ru.practicum.ewm.core.interaction.dto.event.EventShortDto;
import ru.practicum.ewm.core.interaction.dto.event.LocationDto;
import ru.practicum.ewm.core.interaction.dto.event.NewEventDto;
import ru.practicum.ewm.core.interaction.dto.user.UserDto;
import ru.practicum.ewm.core.interaction.dto.user.UserShortDto;
import ru.practicum.ewm.core.events.entity.Category;
import ru.practicum.ewm.core.events.entity.Event;
import ru.practicum.ewm.core.interaction.enums.EventState;
import ru.practicum.ewm.core.interaction.util.DateFormatter;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring",
        uses = {CategoryMapper.class, LocationMapper.class},
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
    Event toEvent(NewEventDto newEventDto, Category category, Long initiator,
                  LocationDto location);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "eventDate", expression = "java(formatDate(event.getEventDate()))")
    @Mapping(target = "createdOn", expression = "java(formatDate(event.getCreatedOn()))")
    @Mapping(target = "publishedOn", expression = "java(formatDate(event.getPublishedOn()))")
    @Mapping(target = "state", expression = "java(event.getState().name())")
    @Mapping(target = "initiator", ignore = true)
    EventFullDto toEventFullDto(Event event, UserDto user);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "event.id")
    @Mapping(target = "eventDate", expression = "java(formatDate(event.getEventDate()))")
    @Mapping(target = "initiator", ignore = true)
    EventShortDto toEventShortDto(Event event, UserDto user);

    @AfterMapping
    default void setInitiator(@MappingTarget EventShortDto dto, UserDto user) {
        if (user == null) {
            return;
        }

        UserShortDto shortDto = new UserShortDto();
        shortDto.setId(user.getId());
        shortDto.setName(user.getName());
        dto.setInitiator(shortDto);
    }

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

    @Mapping(target = "initiator", ignore = true)
    @Mapping(target = "initiatorId", source = "initiator")
    EventFullDto toEventFullDto(Event event);

}