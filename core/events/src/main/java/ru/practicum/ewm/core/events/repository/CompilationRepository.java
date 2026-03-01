package ru.practicum.ewm.core.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.core.events.entity.Compilation;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {



}