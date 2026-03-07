package ru.practicum.ewm.core.interaction.apiinterface.priv;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.core.interaction.dto.comment.CommentDto;
import ru.practicum.ewm.core.interaction.dto.comment.CreateCommentDto;

import java.util.List;

public interface PrivateCommentOperations {

    @PostMapping("/{userId}/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    CommentDto create(@PathVariable(name = "eventId") Long eventId,
                      @PathVariable(name = "userId") Long userId,
                      @RequestBody CreateCommentDto createCommentDto);

    @PatchMapping("/{userId}/comments/{commentId}")
    CommentDto update(@PathVariable(name = "userId") Long userId,
                      @PathVariable(name = "commentId") Long commentId,
                      @RequestBody CreateCommentDto createCommentDto);

    @DeleteMapping("/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable(name = "userId") Long userId,
                @PathVariable(name = "commentId") Long commentId);

    @GetMapping("/{userId}/comments/search")
    List<CommentDto> findCommentByText(@RequestParam(name = "query", defaultValue = "") String text);

}