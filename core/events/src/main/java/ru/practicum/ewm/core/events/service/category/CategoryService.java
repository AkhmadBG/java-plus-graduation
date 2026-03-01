package ru.practicum.ewm.core.events.service.category;

import ru.practicum.ewm.core.interaction.dto.category.CategoryDto;
import ru.practicum.ewm.core.interaction.dto.category.NewCategoryDto;
import ru.practicum.ewm.core.interaction.dto.category.UpdateCategoryDto;

import java.util.List;

public interface CategoryService {

    void delete(Long id);

    List<CategoryDto> getAll(int from, int size);

    CategoryDto getById(Long id);

    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    CategoryDto updateCategory(Long id, UpdateCategoryDto updateCategoryDto);

}