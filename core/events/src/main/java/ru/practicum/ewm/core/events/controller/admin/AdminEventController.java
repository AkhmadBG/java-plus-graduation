package ru.practicum.ewm.core.events.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.core.events.service.event.EventService;
import ru.practicum.ewm.core.interaction.apiinterface.adm.AdminEventOperations;
import ru.practicum.ewm.core.interaction.dto.event.AdminEventSearchRequest;
import ru.practicum.ewm.core.interaction.dto.event.EventFullDto;
import ru.practicum.ewm.core.interaction.dto.event.UpdateEventAdminDto;
import ru.practicum.ewm.core.interaction.enums.EventState;

import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Validated
public class AdminEventController implements AdminEventOperations {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getEvents(
            @RequestParam(name = "users", required = false) List<Long> users,
            @RequestParam(name = "states", required = false) List<EventState> states,
            @RequestParam(name = "categories", required = false) List<Long> categories,
            @RequestParam(name = "rangeStart", required = false) String rangeStart,
            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {

        return eventService.getEventsWithParamsByAdmin(
                AdminEventSearchRequest.fromParams(
                        users, states, categories, rangeStart, rangeEnd, from, size));
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable(name = "eventId") Long eventId,
                                    @Valid @RequestBody UpdateEventAdminDto updateEventAdminDto) {
        return eventService.updateEvent(eventId, updateEventAdminDto);
    }

    @PatchMapping("/{eventId}/set/{count}")
    public void setConfirmedRequests(@PathVariable(name = "eventId") Long eventId,
                                    @PathVariable(name = "count") Long count) {
        eventService.setConfirmedRequests(eventId, count);
    }

}