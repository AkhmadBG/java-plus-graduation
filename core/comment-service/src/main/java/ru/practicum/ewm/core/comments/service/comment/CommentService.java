package ru.practicum.ewm.core.comments.service.comment;

import ru.practicum.ewm.core.interaction.dto.comment.CommentDto;
import ru.practicum.ewm.core.interaction.dto.comment.CreateCommentDto;
import ru.practicum.ewm.core.interaction.dto.event.EventFullDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> getEventComments(Long eventId, int from, int size);

    CommentDto create(Long eventId, Long userId, CreateCommentDto createCommentDto);

    CommentDto update(Long commentId, Long userId, CreateCommentDto createCommentDto);

    void delete(Long userId, Long commentId);

    List<CommentDto> findCommentByText(String text);

    CommentDto getCommentById(Long commentId);

    List<EventFullDto> getTopEvent(Long count);

    List<Long> getTopEventIdList(Long countId);

}