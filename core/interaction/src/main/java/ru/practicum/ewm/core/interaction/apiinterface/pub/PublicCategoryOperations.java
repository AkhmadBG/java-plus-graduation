package ru.practicum.ewm.core.interaction.apiinterface.pub;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.ewm.core.interaction.dto.category.CategoryDto;

import java.util.List;

public interface PublicCategoryOperations {

    @GetMapping
    List<CategoryDto> getAll(@RequestParam(defaultValue = "0") int from,
                                    @RequestParam(defaultValue = "10") int size);

    @GetMapping("/{catId}")
    CategoryDto getById(@PathVariable Long catId);

}
