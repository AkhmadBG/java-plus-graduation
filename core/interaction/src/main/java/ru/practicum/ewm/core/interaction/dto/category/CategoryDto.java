package ru.practicum.ewm.core.interaction.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class CategoryDto {

    private Long id;

    @NotBlank
    private String name;

}