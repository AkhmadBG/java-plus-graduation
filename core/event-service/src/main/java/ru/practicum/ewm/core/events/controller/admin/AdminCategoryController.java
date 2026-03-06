package ru.practicum.ewm.core.events.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.core.events.service.category.CategoryService;
import ru.practicum.ewm.core.interaction.apiinterface.adm.AdminCategoryOperations;
import ru.practicum.ewm.core.interaction.dto.category.CategoryDto;
import ru.practicum.ewm.core.interaction.dto.category.NewCategoryDto;
import ru.practicum.ewm.core.interaction.dto.category.UpdateCategoryDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminCategoryController implements AdminCategoryOperations {

    private final CategoryService categoryService;

    @PostMapping("/categories")
    public ResponseEntity<CategoryDto> create(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.addCategory(newCategoryDto));
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long catId) {
        categoryService.delete(catId);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto update(@PathVariable Long catId,
                              @Valid @RequestBody UpdateCategoryDto updateCategoryDto) {
        return categoryService.updateCategory(catId, updateCategoryDto);
    }

}