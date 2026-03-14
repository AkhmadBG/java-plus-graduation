package ru.practicum.ewm.stats.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.stats.analyzer.entity.Similarity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SimilarityRepository extends JpaRepository<Similarity, Long> {

    Optional<Similarity> findByEvent1AndEvent2(long eventA, long eventB);

    List<Similarity> findSimilarEventsForEvents(Set<Long> viewedEvents);

    List<Similarity> findSimilarEvents(long eventId);

    Optional<Similarity> findSimilarity(Long eventId, long targetEvent);

}
