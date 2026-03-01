package ru.practicum.ewm.core.interaction.apiinterface.pub;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.ewm.core.interaction.dto.event.EventFullDto;
import ru.practicum.ewm.core.interaction.enums.SortValue;

import java.util.List;

public interface PublicEventOperations {

    @GetMapping
    List<EventFullDto> getEvents(
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "categories", required = false) List<Long> categories,
            @RequestParam(name = "paid", required = false) Boolean paid,
            @RequestParam(name = "rangeStart", required = false) String rangeStart,
            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(name = "onlyAvailable", required = false) Boolean onlyAvailable,
            @RequestParam(name = "sort", required = false) SortValue sort,
            @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            HttpServletRequest request);

    @GetMapping("/{id}")
    EventFullDto getEvent(@PathVariable Long id, HttpServletRequest request);

    @GetMapping("/event/info/{eventId}")
    EventFullDto getEventFullDto(@PathVariable Long eventId, Long userId);

    @GetMapping("/{eventId}/exists")
    Boolean eventExists(@PathVariable Long eventId);

    @GetMapping("/comments/top")
    List<EventFullDto> getTopEvents(@RequestParam(name = "count", defaultValue = "5") Long count);

}