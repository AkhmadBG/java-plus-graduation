package ru.practicum.ewm.core.users.service.user;

import ru.practicum.ewm.core.interaction.dto.user.NewUserRequest;
import ru.practicum.ewm.core.interaction.dto.user.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers(List<Long> ids, int from, int size);

    UserDto createUser(NewUserRequest newUser);

    void deleteUser(Long userId);

}