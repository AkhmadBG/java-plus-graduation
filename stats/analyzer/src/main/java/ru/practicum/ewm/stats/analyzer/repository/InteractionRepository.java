package ru.practicum.ewm.stats.analyzer.repository;

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
    @Query("""
            SELECT i.eventId, SUM(i.rating)
            FROM Interaction i
            WHERE i.eventId IN :eventIds
            GROUP BY i.eventId
            """)
    List<Object[]> sumRatingsByEventIds(@Param("eventIds") List<Long> eventIds);

    List<Interaction> findRecentInteractions(long userId, int limit);

    List<Long> findEventsByUserId(long userId);

    List<Interaction> findByUserId(long userId);

}