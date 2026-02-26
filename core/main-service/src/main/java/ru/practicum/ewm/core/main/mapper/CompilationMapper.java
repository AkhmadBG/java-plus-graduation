package ru.practicum.ewm.core.main.mapper;

import org.mapstruct.*;
import ru.practicum.ewm.core.main.dto.compilation.CompilationDto;
import ru.practicum.ewm.core.main.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.core.main.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.core.main.entity.Compilation;
import ru.practicum.ewm.core.main.entity.Event;

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
