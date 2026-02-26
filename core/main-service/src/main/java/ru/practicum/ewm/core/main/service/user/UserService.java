package ru.practicum.ewm.core.main.service.user;

import ru.practicum.ewm.core.main.dto.user.NewUserRequest;
import ru.practicum.ewm.core.main.dto.user.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers(List<Long> ids, int from, int size);

    UserDto createUser(NewUserRequest newUser);

    void deleteUser(Long userId);
}