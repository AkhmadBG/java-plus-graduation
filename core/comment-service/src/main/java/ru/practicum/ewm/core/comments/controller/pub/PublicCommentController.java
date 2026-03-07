package ru.practicum.ewm.core.comments.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.core.comments.service.comment.CommentService;
import ru.practicum.ewm.core.interaction.apiinterface.pub.PublicCommentOperations;
import ru.practicum.ewm.core.interaction.dto.comment.CommentDto;
import ru.practicum.ewm.core.interaction.dto.event.EventFullDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PublicCommentController implements PublicCommentOperations {

    private final CommentService commentService;

    @GetMapping("/{eventId}/comments")
    public List<CommentDto> getEventComments(@PathVariable(name = "eventId") Long eventId,
                                             @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        return commentService.getEventComments(eventId, from, size);
    }

    @GetMapping("/comments/top")
    public List<EventFullDto> getTopEvents(@RequestParam(name = "count", defaultValue = "5") Long count) {
        return commentService.getTopEvent(count);
    }

    @GetMapping("/comments/{commentId}")
    public CommentDto getCommentById(@PathVariable(name = "commentId") Long commentId) {
        return commentService.getCommentById(commentId);
    }

    @GetMapping("/top/{count}")
    public List<Long> getTopEventIdList(@PathVariable Long count) {
        return commentService.getTopEventIdList(count);
    }

}