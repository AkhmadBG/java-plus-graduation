package ru.practicum.ewm.core.requests.controller.adm;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.core.interaction.apiinterface.adm.AdminRequestOperation;
import ru.practicum.ewm.core.interaction.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.core.requests.service.request.ParticipationRequestService;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class AdminRequestController implements AdminRequestOperation {

    private final ParticipationRequestService service;

    @PostMapping("/by-ids")
    public List<ParticipationRequestDto> getRequestsByIds(@RequestBody List<Long> requestsId) {
        return service.getRequestsByIds(requestsId);
    }

    @PostMapping("/status")
    public void updateRequestsStatus(@RequestBody List<ParticipationRequestDto> requests) {
        service.updateRequestsStatus(requests);
    }

}