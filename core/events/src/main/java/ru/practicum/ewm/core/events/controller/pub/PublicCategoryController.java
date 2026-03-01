package ru.practicum.ewm.core.events.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.core.interaction.apiinterface.pub.PublicCategoryOperations;
import ru.practicum.ewm.core.interaction.dto.category.CategoryDto;
import ru.practicum.ewm.core.events.service.category.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class PublicCategoryController implements PublicCategoryOperations {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getAll(@RequestParam(defaultValue = "0") int from,
                                    @RequestParam(defaultValue = "10") int size) {
        return categoryService.getAll(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getById(@PathVariable Long catId) {
        return categoryService.getById(catId);
    }

}