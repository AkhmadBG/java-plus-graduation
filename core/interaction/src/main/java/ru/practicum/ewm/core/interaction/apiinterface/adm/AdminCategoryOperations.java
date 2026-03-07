package ru.practicum.ewm.core.interaction.apiinterface.adm;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.core.interaction.dto.category.CategoryDto;
import ru.practicum.ewm.core.interaction.dto.category.NewCategoryDto;
import ru.practicum.ewm.core.interaction.dto.category.UpdateCategoryDto;

public interface AdminCategoryOperations {

    @PostMapping("/categories")
    ResponseEntity<CategoryDto> create(@Valid @RequestBody NewCategoryDto newCategoryDto);

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable Long catId);

    @PatchMapping("/categories/{catId}")
    CategoryDto update(@PathVariable Long catId, @Valid @RequestBody UpdateCategoryDto updateCategoryDto);

}