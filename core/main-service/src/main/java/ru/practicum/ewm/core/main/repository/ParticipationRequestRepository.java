package ru.practicum.ewm.core.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.core.main.entity.ParticipationRequest;
import ru.practicum.ewm.core.interaction.enums.RequestStatus;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findByRequester(Long userId);

    Boolean existsByRequesterAndEventId(Long requesterId, Long eventId);

    List<ParticipationRequest> findAllByRequester_AndEvent_Id(Long userId, Long eventId);

    List<ParticipationRequest> findByEventIdAndStatus(Long eventId, RequestStatus status);

    Long countByEventIdAndStatus(Long eventId, RequestStatus status);

    List<ParticipationRequest> findByEventId(Long eventId);
}