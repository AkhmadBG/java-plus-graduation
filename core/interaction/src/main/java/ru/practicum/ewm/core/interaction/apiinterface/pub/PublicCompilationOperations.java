package ru.practicum.ewm.core.interaction.apiinterface.pub;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.ewm.core.interaction.dto.compilation.CompilationDto;

import java.util.List;

public interface PublicCompilationOperations {

    @GetMapping
    List<CompilationDto> getCompilations(@RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size);

    @GetMapping("/{compId}")
    CompilationDto getCompilationById(@PathVariable("compId") Long compId);

}
