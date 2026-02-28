package ru.practicum.ewm.core.interaction.apiinterface.priv;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.core.interaction.dto.request.ParticipationRequestDto;

import java.util.List;

public interface PrivateRequestOperations {

    @GetMapping
    List<ParticipationRequestDto> getRequests(@PathVariable("userId") @NotNull @Positive Long userId);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ParticipationRequestDto addRequest(@PathVariable("userId") @NotNull @Positive Long userId,
                                              @RequestParam("eventId") @NotNull @Positive Long eventId);

    @PatchMapping("/{requestId}/cancel")
    ParticipationRequestDto cancelRequest(@PathVariable("userId") @NotNull @Positive Long userId,
                                                 @PathVariable("requestId") @NotNull @Positive Long requestId);

}