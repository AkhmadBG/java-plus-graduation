package ru.practicum.ewm.core.interaction.apiinterface.adm;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.ewm.core.interaction.dto.request.ParticipationRequestDto;

import java.util.List;

public interface AdminRequestOperation {

    @PostMapping("/by-ids")
    List<ParticipationRequestDto> getRequestsByIds(@RequestBody List<Long> requestsId);

    @PostMapping("/status")
    void updateRequestsStatus(@RequestBody List<ParticipationRequestDto> requests);

}