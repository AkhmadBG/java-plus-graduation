package ru.practicum.ewm.main.dto.event;

import lombok.*;
import ru.practicum.ewm.main.dto.request.ParticipationRequestDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateParticipationRequestListDto {

    @Builder.Default
    private List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();

    @Builder.Default
    private List<ParticipationRequestDto> rejectedRequests =  new ArrayList<>();

}