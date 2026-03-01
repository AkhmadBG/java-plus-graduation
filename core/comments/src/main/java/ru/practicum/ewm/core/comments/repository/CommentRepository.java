package ru.practicum.ewm.core.comments.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.core.main.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByEventId(Long eventId, Pageable pageable);

    Optional<Comment> findByIdAndOwner(Long commentId, Long ownerId);

    void deleteByIdAndOwner(Long commentId, Long ownerId);

    List<Comment> findAllByTextIsLikeIgnoreCase(String text);

}
