package ru.practicum.ewm.core.requests.service.request;

import feign.FeignException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.core.interaction.dto.event.EventFullDto;
import ru.practicum.ewm.core.interaction.dto.event.UpdateParticipationRequestListDto;
import ru.practicum.ewm.core.interaction.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.core.interaction.dto.request.UpdateParticipationRequestDto;
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

        System.out.println("eventFullDto.getInitiator().getId()" + eventFullDto.getInitiator().getId());
        System.out.println("userId" + userId);

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

//        if (eventFullDto.getRequestModeration() && eventFullDto.getParticipantLimit() != 0) {
//            request.setStatus(RequestStatus.PENDING);
//        } else {
//            System.out.println("в ParticipationRequestServiceImpl в блоке setStatus(RequestStatus.CONFIRMED)");
//            request.setStatus(RequestStatus.CONFIRMED);
//            eventFullDto.setConfirmedRequests(eventFullDto.getConfirmedRequests() + 1);
//            System.out.println("в ParticipationRequestServiceImpl eventFullDto.getConfirmedRequests() до отправки на сохранение в БД = " + eventFullDto.getConfirmedRequests());
//            updateEventConfirmedRequests(eventFullDto.getId());
//        }
//
//
//        return requestRepository.save(request);

        if (eventFullDto.getRequestModeration() && eventFullDto.getParticipantLimit() != 0) {
            request.setStatus(RequestStatus.PENDING);
        } else {
            System.out.println("в ParticipationRequestServiceImpl в блоке setStatus(RequestStatus.CONFIRMED)");
            request.setStatus(RequestStatus.CONFIRMED);
        }

        ParticipationRequest saved = requestRepository.save(request);
        System.out.println("в ParticipationRequestServiceImpl eventFullDto.getConfirmedRequests() до отправки на сохранение в БД = " + eventFullDto.getConfirmedRequests());
        updateEventConfirmedRequests(eventId);

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
    public List<ParticipationRequestDto> getUserRequestsByEventId(Long userId, Long eventId) {
        EventFullDto eventFullDto = publicEventFeignClient.getEventFullDto(eventId, userId);

        if (!eventFullDto.getInitiator().getId().equals(userId)) {
            throw new ForbiddenException("User is not the initiator of the event");
        }

        List<ParticipationRequest> requests = requestRepository.findByEvent(eventId);

        return requests.stream()
                .map(participationRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UpdateParticipationRequestListDto updateUserRequestsByEventId(Long userId, Long eventId, UpdateParticipationRequestDto updateDto) {
        EventFullDto eventFullDto = publicEventFeignClient.getEventFullDto(eventId, userId);

        List<Long> requestIds = updateDto.getRequestsId();
        if (requestIds == null || requestIds.isEmpty()) {
            List<ParticipationRequest> pendingRequests = requestRepository.findByEventAndStatus(eventId, RequestStatus.PENDING);
            if (pendingRequests.isEmpty()) {
                throw new ConflictException("No pending requests found for this event");
            }
            requestIds = pendingRequests.stream()
                    .map(ParticipationRequest::getId)
                    .collect(Collectors.toList());
        }

        List<ParticipationRequest> requests = requestRepository.findAllById(requestIds);

        if (requests.size() != requestIds.size()) {
            throw new NotFoundException("Some request IDs were not found");
        }

        for (ParticipationRequest request : requests) {
            if (!request.getEvent().equals(eventId)) {
                throw new ForbiddenException("Request with id=" + request.getId() + " does not belong to this event");
            }

            if (updateDto.getStatus() == RequestStatus.CONFIRMED && request.getStatus() == RequestStatus.CONFIRMED) {
                throw new ConflictException("Request is already confirmed");
            }
        }

        RequestStatus newStatus = updateDto.getStatus();

        if (newStatus == RequestStatus.CONFIRMED) {
            long currentConfirmed = requestRepository.countByEventAndStatus(eventId, RequestStatus.CONFIRMED);
            long limit = eventFullDto.getParticipantLimit() == null ? 0 : eventFullDto.getParticipantLimit();
            if (limit == 0) {
                requests.forEach(r -> r.setStatus(RequestStatus.CONFIRMED));
            } else {
                long availableSlots = limit - currentConfirmed;

                if (availableSlots <= 0) {
                    requests.forEach(r -> r.setStatus(RequestStatus.REJECTED));
                } else {
                    List<ParticipationRequest> toConfirm = requests.stream()
                            .filter(r -> r.getStatus() != RequestStatus.CONFIRMED)
                            .limit(availableSlots)
                            .toList();

                    List<ParticipationRequest> toReject = requests.stream()
                            .filter(r -> !toConfirm.contains(r))
                            .toList();

                    toConfirm.forEach(r -> r.setStatus(RequestStatus.CONFIRMED));
                    toReject.forEach(r -> r.setStatus(RequestStatus.REJECTED));
                }
            }
        } else {
            requests.forEach(r -> r.setStatus(newStatus));
        }
        List<ParticipationRequest> updatedRequests = requestRepository.saveAll(requests);
        updateEventConfirmedRequests(eventId);
        UpdateParticipationRequestListDto result = new UpdateParticipationRequestListDto();

        for (ParticipationRequest request : updatedRequests) {
            if (request.getStatus() == RequestStatus.CONFIRMED) {
                result.getConfirmedRequests().add(participationRequestMapper.toDto(request));
            } else if (request.getStatus() == RequestStatus.REJECTED) {
                result.getRejectedRequests().add(participationRequestMapper.toDto(request));
            }
        }

        return result;
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