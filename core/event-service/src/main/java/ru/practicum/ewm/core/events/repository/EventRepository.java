package ru.practicum.ewm.core.events.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.core.events.entity.Event;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findAllByInitiator(Long userId, Pageable pageable);

    Optional<Event> findByIdAndInitiator(Long eventId, Long userId);

    List<Event> findByIdIn(List<Long> eventIds);

    Boolean existsByCategoryId(Long categoryId);

    Optional<Event> findByIdAndPublishedOnIsNotNull(Long id);

}