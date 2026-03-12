package ru.practicum.ewm.stats.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.stats.analyzer.entity.Similarity;

@Repository
public interface SimilarityRepository extends JpaRepository<Similarity, Long> {
}
