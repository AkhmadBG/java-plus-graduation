package ru.practicum.ewm.core.interaction.apiinterface.adm;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.core.interaction.dto.user.NewUserRequest;
import ru.practicum.ewm.core.interaction.dto.user.UserDto;
import ru.practicum.ewm.core.interaction.dto.user.UserShortDto;

import java.util.List;

public interface AdminUserOperations {

    @GetMapping
    List<UserDto> getUsers(@RequestParam(required = false) @Valid List<Long> ids,
                           @RequestParam(defaultValue = "0") @Valid int from,
                           @RequestParam(defaultValue = "10") @Valid int size);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserDto createUser(@RequestBody @Valid NewUserRequest newUser);

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@PathVariable Long userId);

    @GetMapping("/{userId}/exists")
    Boolean userExists(@PathVariable Long userId);

    @GetMapping("/{userId}")
    UserDto getUser(@PathVariable Long userId);

    @GetMapping("/dto")
    List<UserDto> getUsersByIds(@RequestParam List<Long> ids);

    @GetMapping("/shortdto")
    List<UserShortDto> getUsersShortDtoByIds(@RequestParam List<Long> ids);

}