package ru.practicum.ewm.core.interaction.apiinterface.adm;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.core.interaction.dto.event.EventFullDto;
import ru.practicum.ewm.core.interaction.dto.event.UpdateEventAdminDto;
import ru.practicum.ewm.core.interaction.enums.EventState;

import java.util.List;

public interface AdminEventOperations {

    @GetMapping
    List<EventFullDto> getEvents(
            @RequestParam(name = "users", required = false) List<Long> users,
            @RequestParam(name = "states", required = false) List<EventState> states,
            @RequestParam(name = "categories", required = false) List<Long> categories,
            @RequestParam(name = "rangeStart", required = false) String rangeStart,
            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size);

    @PatchMapping("/{eventId}")
    EventFullDto updateEvent(@PathVariable(name = "eventId") Long eventId,
                                    @Valid @RequestBody UpdateEventAdminDto updateEventAdminDto);

}