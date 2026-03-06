package ru.practicum.ewm.core.comments.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.core.comments.entity.Comment;
import ru.practicum.ewm.core.comments.mapper.CommentMapper;
import ru.practicum.ewm.core.comments.repository.CommentRepository;
import ru.practicum.ewm.core.interaction.dto.comment.CommentDto;
import ru.practicum.ewm.core.interaction.dto.comment.CreateCommentDto;
import ru.practicum.ewm.core.interaction.dto.event.EventFullDto;
import ru.practicum.ewm.core.interaction.dto.user.UserDto;
import ru.practicum.ewm.core.interaction.exceptions.CommentNotExistException;
import ru.practicum.ewm.core.interaction.exceptions.NotFoundException;
import ru.practicum.ewm.core.interaction.feignclient.adm.AdminUserFeignClient;
import ru.practicum.ewm.core.interaction.feignclient.pub.PublicEventFeignClient;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PublicEventFeignClient publicEventFeignClient;
    private final AdminUserFeignClient adminUserFeignClient;
    private final CommentMapper commentMapper;

    @Override
    public List<CommentDto> getEventComments(Long eventId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").descending());
        Page<Comment> comments = commentRepository.findAllByEvent(eventId, pageable);

        Set<Long> ownerIds = comments.stream()
                .map(Comment::getOwner)
                .collect(Collectors.toSet());

        List<UserDto> users = adminUserFeignClient.getUsers(new ArrayList<>(ownerIds), from, size);

        Map<Long, UserDto> userMap = users.stream()
                .collect(Collectors.toMap(UserDto::getId, Function.identity()));

        return comments.getContent()
                .stream()
                .map(comment -> commentMapper.toCommentDto(comment, userMap.get(comment.getOwner()).getName()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto create(Long eventId, Long userId, CreateCommentDto createCommentDto) {
        publicEventFeignClient.eventExists(eventId);

        UserDto user = adminUserFeignClient.getUser(userId);

        Comment commentFromDto = commentMapper.toComment(createCommentDto);

        commentFromDto.setEvent(eventId);
        commentFromDto.setOwner(userId);
        commentFromDto.setCreated(LocalDateTime.now());

        Comment comment = commentRepository.save(commentFromDto);
        return commentMapper.toCommentDto(comment, user.getName());
    }

    @Override
    @Transactional
    public CommentDto update(Long commentId, Long userId, CreateCommentDto createCommentDto) {
        Comment comment = commentRepository.findByIdAndOwner(commentId, userId)
                .orElseThrow(() -> new CommentNotExistException("Not possible update Comment - " +
                        "Does not exist comment with Id " + commentId + " for user with id " + userId));

        comment.setText(createCommentDto.getText());
        UserDto user = adminUserFeignClient.getUser(userId);
        return commentMapper.toCommentDto(commentRepository.save(comment), user.getName());
    }

    @Override
    @Transactional
    public void delete(Long commentId, Long userId) {
        commentRepository.deleteByIdAndOwner(commentId, userId);
    }

    @Override
    public List<CommentDto> findCommentByText(String text) {
        List<Comment> allByTextIsLikeIgnoreCase = commentRepository.findAllByTextIsLikeIgnoreCase(text);

        Set<Long> ownerIds = allByTextIsLikeIgnoreCase.stream()
                .map(Comment::getOwner)
                .collect(Collectors.toSet());

        List<UserDto> users = adminUserFeignClient.getUsersByIds(new ArrayList<>(ownerIds));

        Map<Long, UserDto> userMap = users.stream()
                .collect(Collectors.toMap(UserDto::getId, Function.identity()));

        return allByTextIsLikeIgnoreCase.stream()
                .map(comment -> commentMapper.toCommentDto(comment, userMap.get(comment.getOwner()).getName()))
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("комментарий не найден"));

        UserDto user = adminUserFeignClient.getUser(comment.getOwner());

        return commentMapper.toCommentDto(comment, user.getName());
    }

    @Override
    public List<EventFullDto> getTopEvent(Long count) {
        return publicEventFeignClient.getTopEvents(count);
    }

    @Override
    public List<Long> getTopEventIdList(Long count) {
        return commentRepository.getTopEventIdList(count);
    }

}