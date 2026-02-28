package ru.practicum.ewm.core.users.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.core.interaction.dto.user.NewUserRequest;
import ru.practicum.ewm.core.interaction.dto.user.UserDto;
import ru.practicum.ewm.core.interaction.dto.user.UserShortDto;
import ru.practicum.ewm.core.interaction.exceptions.ConflictException;
import ru.practicum.ewm.core.interaction.exceptions.UserNotExistException;
import ru.practicum.ewm.core.users.entity.User;
import ru.practicum.ewm.core.users.mapper.UserMapper;
import ru.practicum.ewm.core.users.repository.UserRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        Pageable page = PageRequest.of(from / size, size, Sort.by("id").ascending());
        List<User> users;

        if (ids == null || ids.isEmpty()) {
            Page<User> pageResult = repository.findAll(page);
            users = pageResult.getContent();
        } else {
            users = repository.findAllById(ids)
                    .stream()
                    .sorted(Comparator.comparingLong(User::getId))
                    .skip(from)
                    .limit(size)
                    .toList();
        }

        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }


    @Override
    public UserDto createUser(NewUserRequest newUser) {
        String email = newUser.getEmail();
        if (repository.existsByEmail(email)) {
            throw new ConflictException("User with email=%s already exists.".formatted(email));
        }

        User user = userMapper.toEntity(newUser);
        return userMapper.toDto(repository.save(user));
    }

    @Override
    public void deleteUser(Long userId) {
        if (!repository.existsById(userId)) {
            throw new UserNotExistException("User with id=%d not found.".formatted(userId));
        }

        repository.deleteById(userId);
    }

    @Override
    public UserDto getUser(Long userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotExistException("User with id=%d not found.".formatted(userId)));
        return userMapper.toDto(user);
    }

    @Override
    public Boolean userExists(Long userId) {
        return null;
    }

    @Override
    public List<UserDto> getUsersByIds(List<Long> ids) {
        List<User> users = repository.findAllById(ids);
        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public List<UserShortDto> getUserUsersShortDtoByIds(List<Long> ids) {
        List<User> users = repository.findAllById(ids);
        return users.stream()
                .map(userMapper::toUserShortDto)
                .toList();
    }

}