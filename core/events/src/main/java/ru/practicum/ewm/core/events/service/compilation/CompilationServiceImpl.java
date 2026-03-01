package ru.practicum.ewm.core.events.service.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.core.interaction.dto.compilation.CompilationDto;
import ru.practicum.ewm.core.interaction.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.core.interaction.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.core.interaction.dto.event.EventFullDto;
import ru.practicum.ewm.core.interaction.dto.user.UserShortDto;
import ru.practicum.ewm.core.interaction.exceptions.NotFoundException;
import ru.practicum.ewm.core.interaction.feignclient.adm.AdminUserFeignClient;
import ru.practicum.ewm.core.events.entity.Compilation;
import ru.practicum.ewm.core.events.entity.Event;
import ru.practicum.ewm.core.events.mapper.CompilationMapper;
import ru.practicum.ewm.core.events.repository.CompilationRepository;
import ru.practicum.ewm.core.events.service.event.EventServiceImpl;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventServiceImpl eventServiceImpl;
    private final AdminUserFeignClient adminUserFeignClient;
    private final CompilationMapper compilationMapper;

//    @Override
//    public List<CompilationDto> getCompilations(int from, int size) {
//        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").descending());
//        Page<Compilation> compilations = compilationRepository.findAll(pageable);
//        return compilations.getContent()
//                .stream()
//                .map(compilationMapper::toCompilationDto)
//                .collect(Collectors.toList());
//    }

    @Override
    public List<CompilationDto> getCompilations(int from, int size) {

        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").descending());
        List<Compilation> compilations = compilationRepository.findAll(pageable).getContent();

        List<CompilationDto> dtos = compilations.stream()
                .map(compilationMapper::toCompilationDto)
                .collect(Collectors.toList());

        Set<Long> userIds = dtos.stream()
                .flatMap(c -> c.getEvents().stream())
                .map(EventFullDto::getInitiatorId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<Long> userIdList = new ArrayList<>(userIds);

        Map<Long, UserShortDto> usersMap = adminUserFeignClient
                .getUsersShortDtoByIds(userIdList)
                .stream()
                .collect(Collectors.toMap(
                        UserShortDto::getId,   // ключ
                        user -> user            // значение
                ));

        dtos.forEach(compilation ->
                compilation.getEvents().forEach(event ->
                        event.setInitiator(usersMap.get(event.getInitiatorId()))
                )
        );

        return dtos;
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository
                .findById(compId).orElseThrow(() -> new NotFoundException("подборка не найдена"));
        return compilationMapper.toCompilationDto(compilation);
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = new ArrayList<>();
        if (newCompilationDto.hasEvents()) {
            events = newCompilationDto.getEvents().stream()
                    .map(eventServiceImpl::getEventById)
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        if (!newCompilationDto.hasPinned()) {
            newCompilationDto.setPinned(false);
        }
        Compilation compilation = compilationMapper.toCompilation(newCompilationDto, events);
        Compilation newCompilation = compilationRepository.save(compilation);
        return compilationMapper.toCompilationDto(newCompilation);
    }

    @Override
    public void deleteCompilationById(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilationById(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("подборка не найдена"));

        compilationMapper.updateCompilationFields(updateCompilationRequest, compilation);

        if (updateCompilationRequest.hasEvents()) {
            List<Event> events = getEventsFromIds(updateCompilationRequest.getEvents());
            compilation.setEvents(events);
        }

        Compilation updatedCompilation = compilationRepository.save(compilation);
        return compilationMapper.toCompilationDto(updatedCompilation);
    }

    private List<Event> getEventsFromIds(Set<Long> eventIds) {
        if (eventIds == null || eventIds.isEmpty()) {
            return new ArrayList<>();
        }
        return eventIds.stream()
                .map(eventServiceImpl::getEventById)
                .collect(Collectors.toCollection(ArrayList::new));
    }

}