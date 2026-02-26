package ru.practicum.ewm.core.main.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserShortDto {

    @NotNull
    private Long id;

    @NotBlank
    private String name;
}
