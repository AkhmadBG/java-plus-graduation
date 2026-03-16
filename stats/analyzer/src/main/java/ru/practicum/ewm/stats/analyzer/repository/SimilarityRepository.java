package ru.practicum.ewm.stats.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.stats.analyzer.entity.Similarity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SimilarityRepository extends JpaRepository<Similarity, Long> {

    Optional<Similarity> findByEvent1AndEvent2(Long event1, Long event2);

    @Query("""
            SELECT s
            FROM Similarity s
            WHERE s.event1 = :eventId OR s.event2 = :eventId
            """)
    List<Similarity> findSimilarEvents(@Param("eventId") Long eventId);

    @Query("""
            SELECT s
            FROM Similarity s
            WHERE s.event1 IN :eventIds
            """)
    List<Similarity> findSimilarEventsForEvents(@Param("eventIds") Set<Long> eventIds);

}