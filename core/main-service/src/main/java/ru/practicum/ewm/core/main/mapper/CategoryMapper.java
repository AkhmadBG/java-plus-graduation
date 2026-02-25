package ru.practicum.ewm.core.main.mapper;

import org.mapstruct.*;
import ru.practicum.ewm.core.main.dto.category.CategoryDto;
import ru.practicum.ewm.core.main.dto.category.NewCategoryDto;
import ru.practicum.ewm.core.main.dto.category.UpdateCategoryDto;
import ru.practicum.ewm.core.main.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto toCategoryDto(Category category);

    Category toEntity(CategoryDto categoryDto);

    @Mapping(target = "id", ignore = true)
    Category toCategory(NewCategoryDto newCategoryDto);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCategory(UpdateCategoryDto updateCategoryDto, @MappingTarget Category category);

}