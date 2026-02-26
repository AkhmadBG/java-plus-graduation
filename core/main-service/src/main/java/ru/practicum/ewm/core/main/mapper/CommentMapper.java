package ru.practicum.ewm.core.main.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.core.main.dto.comment.CommentDto;
import ru.practicum.ewm.core.main.dto.comment.CreateCommentDto;
import ru.practicum.ewm.core.main.entity.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "owner.name", target = "ownerName")
    @Mapping(source = "event.id", target = "event")
    CommentDto toCommentDto(Comment comment);

    @Mapping(source = "eventId", target = "owner.id")
    @Mapping(source = "eventId", target = "event.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    Comment toComment(CreateCommentDto createCommentDto);

}