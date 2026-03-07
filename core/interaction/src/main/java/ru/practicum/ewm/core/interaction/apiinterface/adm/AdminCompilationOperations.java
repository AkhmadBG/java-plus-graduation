package ru.practicum.ewm.core.interaction.apiinterface.adm;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.core.interaction.dto.compilation.CompilationDto;
import ru.practicum.ewm.core.interaction.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.core.interaction.dto.compilation.UpdateCompilationRequest;

public interface AdminCompilationOperations {

    @PostMapping
    ResponseEntity<CompilationDto> createCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto);

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCompilationById(@PathVariable("compId") Long compId);

    @PatchMapping("/{compId}")
    CompilationDto updateCompilationById(@PathVariable("compId") Long compId,
                                         @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest);

}