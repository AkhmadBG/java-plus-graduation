package ru.practicum.ewm.core.comments.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.core.comments.entity.Comment;
import ru.practicum.ewm.core.interaction.dto.comment.CommentDto;
import ru.practicum.ewm.core.interaction.dto.comment.CreateCommentDto;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "ownerName")
    @Mapping(source = "comment.event", target = "event")
    CommentDto toCommentDto(Comment comment, String ownerName);

    @Mapping(source = "userId", target = "owner")
    @Mapping(source = "eventId", target = "event")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    Comment toComment(CreateCommentDto createCommentDto);

}