package ru.practicum.ewm.core.events.controller.priv;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.core.events.service.event.EventService;
import ru.practicum.ewm.core.interaction.apiinterface.priv.PrivateEventOperations;
import ru.practicum.ewm.core.interaction.dto.event.*;
import ru.practicum.ewm.core.interaction.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.core.interaction.dto.request.UpdateParticipationRequestDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class PrivateEventController implements PrivateEventOperations {

    private final EventService eventService;

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getEvents(@PathVariable Long userId,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size) {
        return eventService.getEvents(userId, from, size);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable Long userId,
                                    @Valid @RequestBody NewEventDto newEventDto) {
        return eventService.createEvent(userId, newEventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEvent(@PathVariable Long userId,
                                 @PathVariable Long eventId) {
        return eventService.getEventByUser(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEventByUser(@PathVariable Long userId,
                                          @PathVariable Long eventId,
                                          @Valid @RequestBody UpdateEventUserDto updateEventUserDto) {
        return eventService.updateEventByUser(userId, eventId, updateEventUserDto);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getUserRequestsByEventId(@PathVariable Long userId,
                                                                  @PathVariable Long eventId) {
        return eventService.getUserRequestsByEventId(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public UpdateParticipationRequestListDto updateUserRequestsByEventId(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @Valid @RequestBody UpdateParticipationRequestDto updateParticipationRequestDto) {

        log.info("Received update request: userId={}, eventId={}, dto={}",
                userId, eventId, updateParticipationRequestDto.toString());

        return eventService.updateUserRequestsByEventId(userId, eventId, updateParticipationRequestDto);
    }

}