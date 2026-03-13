package ru.practicum.ewm.core.requests.service.request;

import com.google.protobuf.Timestamp;
import feign.FeignException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.client.stats.CollectorClient;
import ru.practicum.ewm.core.interaction.dto.event.EventFullDto;
import ru.practicum.ewm.core.interaction.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.core.interaction.exceptions.CommentNotExistException;
import ru.practicum.ewm.core.interaction.exceptions.ConflictException;
import ru.practicum.ewm.core.interaction.exceptions.ForbiddenException;
import ru.practicum.ewm.core.interaction.exceptions.NotFoundException;
import ru.practicum.ewm.core.interaction.feignclient.adm.AdminEventFeignClient;
import ru.practicum.ewm.core.interaction.feignclient.adm.AdminUserFeignClient;
import ru.practicum.ewm.core.interaction.enums.EventState;
import ru.practicum.ewm.core.interaction.enums.RequestStatus;
import ru.practicum.ewm.core.interaction.feignclient.pub.PublicEventFeignClient;
import ru.practicum.ewm.core.requests.entity.ParticipationRequest;
import ru.practicum.ewm.core.requests.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.core.requests.repository.ParticipationRequestRepository;
import ru.practicum.ewm.stats.proto.ActionTypeProto;
import ru.practicum.ewm.stats.proto.UserActionProto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final AdminEventFeignClient adminEventFeignClient;
    private final PublicEventFeignClient publicEventFeignClient;
    private final AdminUserFeignClient adminUserFeignClient;
    private final ParticipationRequestRepository requestRepository;
    private final ParticipationRequestMapper participationRequestMapper;
    private final CollectorClient collectorClient;

    @Transactional
    public List<ParticipationRequest> getUserRequests(Long userId) {
        Boolean userExists = adminUserFeignClient.userExists(userId);
        if (!userExists) {
            throw new CommentNotExistException("Not possible create Comment - " + "Does not exist User with Id " + userId);
        }
        return requestRepository.findAllByRequester(userId);
    }

    @Transactional
    public ParticipationRequest addRequest(Long userId, Long eventId) {
        Boolean userExists = adminUserFeignClient.userExists(userId);

        if (!userExists) {
            throw new CommentNotExistException("Not possible create Comment - " + "Does not exist User with Id " + userId);
        }

        EventFullDto eventFullDto;

        try {
            eventFullDto = publicEventFeignClient.getEventFullDtoForRequest(eventId, userId);
        } catch (FeignException.Conflict e) {
            throw new ConflictException("Event is not published");
        }

        long confirmedCount =
                requestRepository.countByEventAndStatus(eventId, RequestStatus.CONFIRMED);

        if (eventFullDto.getParticipantLimit() > 0 &&
                confirmedCount >= eventFullDto.getParticipantLimit()) {
            throw new ConflictException("Participant limit reached");
        }

        if (eventFullDto.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Initiator cannot request own event");
        }

        if (EventState.valueOf(eventFullDto.getState()) != EventState.PUBLISHED) {
            throw new ConflictException("Event is not published");
        }

        if (requestRepository.existsByRequesterAndEvent(userId, eventId)) {
            throw new ConflictException("Request already exists");
        }

        ParticipationRequest request = new ParticipationRequest();
        request.setRequester(userId);
        request.setEvent(eventId);
        request.setCreated(LocalDateTime.now());

        if (eventFullDto.getRequestModeration() && eventFullDto.getParticipantLimit() != 0) {
            request.setStatus(RequestStatus.PENDING);
        } else {
            request.setStatus(RequestStatus.CONFIRMED);
        }

        ParticipationRequest saved = requestRepository.save(request);
        updateEventConfirmedRequests(eventId);

        Instant now = Instant.now();
        Timestamp timestamp = Timestamp.newBuilder()
                .setSeconds(now.getEpochSecond())
                .setNanos(now.getNano())
                .build();

        UserActionProto userActionProto = UserActionProto.newBuilder()
                .setEventId(eventId)
                .setUserId(userId)
                .setActionType(ActionTypeProto.ACTION_REGISTER)
                .setTimestamp(timestamp)
                .build();

        collectorClient.collectUserAction(userActionProto);

        return saved;
    }

    @Transactional
    public ParticipationRequest cancelRequest(Long userId, Long requestId) {
        ParticipationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request with id=%d was not found".formatted(requestId)));

        if (!request.getRequester().equals(userId)) {
            throw new ForbiddenException("Cannot cancel another user's request");
        }

        request.setStatus(RequestStatus.CANCELED);
        return requestRepository.save(request);
    }

    @Override
    public List<ParticipationRequestDto> getRequestsByEventId(Long userId, Long eventId) {
        List<ParticipationRequest> requests = requestRepository.findByEvent(eventId);
        return requests.stream()
                .map(participationRequestMapper::toDto)
                .toList();
    }

    @Override
    public List<ParticipationRequestDto> getRequestsByIds(List<Long> requestsId) {

        List<ParticipationRequest> requests = requestRepository.findAllById(requestsId);

        if (requests.size() != requestsId.size()) {
            throw new NotFoundException("Some requests were not found");
        }

        return requests.stream()
                .map(participationRequestMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void updateRequestsStatus(List<ParticipationRequestDto> requests) {

        Map<Long, ParticipationRequestDto> dtoMap = requests.stream()
                .collect(Collectors.toMap(ParticipationRequestDto::getId, r -> r));

        List<ParticipationRequest> entities = requestRepository.findAllById(dtoMap.keySet());

        for (ParticipationRequest entity : entities) {
            ParticipationRequestDto dto = dtoMap.get(entity.getId());
            entity.setStatus(dto.getStatus());
        }

        requestRepository.saveAll(entities);
    }

    private void updateEventConfirmedRequests(Long eventId) {
        Long confirmedCount = requestRepository.countByEventAndStatus(eventId, RequestStatus.CONFIRMED);
        adminEventFeignClient.setConfirmedRequests(eventId, confirmedCount);
        log.info("Updated confirmedRequests to {} for event {}", confirmedCount, eventId);
    }

}