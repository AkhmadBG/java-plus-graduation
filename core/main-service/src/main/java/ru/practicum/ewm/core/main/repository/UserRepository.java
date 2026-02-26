package ru.practicum.ewm.core.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.core.main.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
}