package ru.practicum.ewm.core.main.service.request;

import ru.practicum.ewm.core.main.dto.event.UpdateParticipationRequestListDto;
import ru.practicum.ewm.core.main.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.core.main.dto.request.UpdateParticipationRequestDto;
import ru.practicum.ewm.core.main.entity.ParticipationRequest;

import java.util.List;

public interface ParticipationRequestService {
    List<ParticipationRequest> getUserRequests(Long userId);

    ParticipationRequest addRequest(Long userId, Long eventId);

    ParticipationRequest cancelRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> getUserRequestsByEventId(Long userId, Long eventId);

    UpdateParticipationRequestListDto updateUserRequestsByEventId(Long userId, Long eventId, UpdateParticipationRequestDto updateParticipationRequestDto);
}
