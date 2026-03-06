package ru.practicum.ewm.core.comments.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.core.comments.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByEvent(Long eventId, Pageable pageable);

    Optional<Comment> findByIdAndOwner(Long commentId, Long ownerId);

    void deleteByIdAndOwner(Long commentId, Long ownerId);

    List<Comment> findAllByTextIsLikeIgnoreCase(String text);

//    @Query("""
//            SELECT c.event FROM Comment c
//            GROUP BY c.event
//            ORDER BY COUNT(c) DESC
//            LIMIT :count
//            """)
//    List<Long> getTopEventIdList(@Param("count") Long count);

    @Query(value = """
       SELECT event_id
       FROM comments
       GROUP BY event_id
       ORDER BY COUNT(id) DESC
       LIMIT :count
       """, nativeQuery = true)
    List<Long> getTopEventIdList(@Param("count") Long count);

}
