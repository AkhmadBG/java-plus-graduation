package ru.practicum.ewm.core.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.core.main.entity.Compilation;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {



}