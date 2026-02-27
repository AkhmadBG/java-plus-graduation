package ru.practicum.ewm.core.main.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.core.interaction.dto.event.LocationDto;
import ru.practicum.ewm.core.main.entity.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    LocationDto toDto(Location location);

    @Mapping(target = "id", source = "id")
    Location toEntity(LocationDto locationDto);
}