package ru.practicum.ewm.stats.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.stats.analyzer.entity.Interaction;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction, Long> {
}
