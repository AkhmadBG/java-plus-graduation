package ru.practicum.ewm.core.interaction.apiinterface.pub;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.ewm.core.interaction.dto.comment.CommentDto;
import ru.practicum.ewm.core.interaction.dto.event.EventFullDto;

import java.util.List;

public interface PublicCommentOperations {

    @GetMapping("/{eventId}/comments")
    List<CommentDto> getEventComments(@PathVariable(name = "eventId") Long eventId,
                                             @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size);

    @GetMapping("/comments/top")
    List<EventFullDto> getTopEvents(@RequestParam(name = "count", defaultValue = "5") int count);

    @GetMapping("/comments/{commentId}")
    CommentDto getCommentById(@PathVariable(name = "commentId") Long commentId);

}
