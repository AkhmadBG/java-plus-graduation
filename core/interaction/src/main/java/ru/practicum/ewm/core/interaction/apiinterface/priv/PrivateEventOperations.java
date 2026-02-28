package ru.practicum.ewm.core.interaction.apiinterface.priv;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.core.interaction.dto.event.*;
import ru.practicum.ewm.core.interaction.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.core.interaction.dto.request.UpdateParticipationRequestDto;

import java.util.List;

public interface PrivateEventOperations {

    @GetMapping("/{userId}/events")
    List<EventShortDto> getEvents(@PathVariable Long userId,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size);

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    EventFullDto createEvent(@PathVariable Long userId,
                             @Valid @RequestBody NewEventDto newEventDto);

    @GetMapping("/{userId}/events/{eventId}")
    EventFullDto getEvent(@PathVariable Long userId,
                          @PathVariable Long eventId);

    @PatchMapping("/{userId}/events/{eventId}")
    EventFullDto updateEventByUser(@PathVariable Long userId,
                                   @PathVariable Long eventId,
                                   @Valid @RequestBody UpdateEventUserDto updateEventUserDto);

    @GetMapping("/{userId}/events/{eventId}/requests")
    List<ParticipationRequestDto> getUserRequestsByEventId(@PathVariable Long userId,
                                                           @PathVariable Long eventId);

    @PatchMapping("/{userId}/events/{eventId}/requests")
    UpdateParticipationRequestListDto updateUserRequestsByEventId(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @Valid @RequestBody UpdateParticipationRequestDto updateParticipationRequestDto);

}