package ru.practicum.ewm.core.events.service.compilation;

import ru.practicum.ewm.core.interaction.dto.compilation.CompilationDto;
import ru.practicum.ewm.core.interaction.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.core.interaction.dto.compilation.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getCompilations(int page, int size);

    CompilationDto getCompilationById(Long compId);

    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilationById(Long compId);

    CompilationDto updateCompilationById(Long compId, UpdateCompilationRequest updateCompilationRequest);

}
