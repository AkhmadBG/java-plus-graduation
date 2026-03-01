package ru.practicum.ewm.core.events.service.event;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import ru.practicum.ewm.core.events.entity.Event;
import ru.practicum.ewm.core.interaction.dto.event.*;
import ru.practicum.ewm.core.interaction.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.core.interaction.dto.request.UpdateParticipationRequestDto;

import java.util.List;

public interface EventService {

    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    List<EventShortDto> getEvents(Long userId, int from, int size);

    EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserDto updateEventUserDto);

    EventFullDto getEventByUser(Long userId, Long eventId);

    EventFullDto updateEvent(Long eventId, UpdateEventAdminDto updateEventAdminDto);

    EventFullDto getEvent(Long id, HttpServletRequest request);

    List<EventFullDto> getEventsWithParamsByAdmin(AdminEventSearchRequest request);

    List<EventFullDto> getEventsWithParamsByUser(PublicEventSearchRequest request, HttpServletRequest httpRequest);

    Event getEventById(Long eventId);

    List<EventFullDto> getTopEvent(Long count);

    EventFullDto getEventFullDto(Long id, Long userId);

    void setConfirmedRequests(Long eventId, Long count);

    Boolean eventExists(Long eventId);

    List<ParticipationRequestDto> getUserRequestsByEventId(Long userId, Long eventId);

    UpdateParticipationRequestListDto updateUserRequestsByEventId(Long userId, Long eventId, @Valid UpdateParticipationRequestDto updateParticipationRequestDto);

}