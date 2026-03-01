package ru.practicum.ewm.core.events.mapper;

import org.mapstruct.*;
import ru.practicum.ewm.core.interaction.dto.compilation.CompilationDto;
import ru.practicum.ewm.core.interaction.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.core.interaction.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.core.events.entity.Compilation;
import ru.practicum.ewm.core.events.entity.Event;

import java.util.List;

@Mapper(componentModel = "spring", uses = EventMapper.class)
public interface CompilationMapper {

    CompilationDto toCompilationDto(Compilation compilation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", source = "newCompilationDto.title")
    @Mapping(target = "pinned", source = "newCompilationDto.pinned")
    @Mapping(target = "events", source = "events")
    Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> events);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateCompilationFields(UpdateCompilationRequest updateCompilationRequest,
                                 @MappingTarget Compilation compilation);
}
