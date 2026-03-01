package ru.practicum.ewm.core.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.core.interaction.enums.RequestStatus;
import ru.practicum.ewm.core.requests.entity.ParticipationRequest;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findByRequester(Long userId);

    Boolean existsByRequesterAndEvent(Long requesterId, Long eventId);

    List<ParticipationRequest> findAllByRequester_AndEvent(Long userId, Long eventId);

    List<ParticipationRequest> findByEventAndStatus(Long eventId, RequestStatus status);

    Long countByEventAndStatus(Long eventId, RequestStatus status);

    List<ParticipationRequest> findByEvent(Long eventId);

}