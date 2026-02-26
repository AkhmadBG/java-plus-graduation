package ru.practicum.ewm.core.main.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.core.main.dto.user.NewUserRequest;
import ru.practicum.ewm.core.main.dto.user.UserDto;
import ru.practicum.ewm.core.main.dto.user.UserShortDto;
import ru.practicum.ewm.core.main.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    User toEntity(NewUserRequest dto);

    UserShortDto toUserShortDto(User user);

}