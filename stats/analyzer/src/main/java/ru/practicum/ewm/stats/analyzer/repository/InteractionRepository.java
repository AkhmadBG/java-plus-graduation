package ru.practicum.ewm.stats.analyzer.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.stats.analyzer.entity.Interaction;

import java.util.List;
import java.util.Optional;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction, Long> {

    Optional<Interaction> findByUserIdAndEventId(Long userId, Long eventId);

    //    List<Interaction> sumRatingsByEventIds(List<Long> eventIds);
    List<Interaction> findByEventIdIn(List<Long> eventIds);

    //    List<Interaction> findRecentInteractions(long userId, int limit);
    List<Interaction> findByUserIdOrderByCreatedDesc(Long userId, Pageable pageable);

    List<Long> findEventsByUserId(long userId);

    List<Interaction> findByUserId(long userId);

}