package ru.practicum.ewm.core.interaction.dto.request;

import lombok.*;
import ru.practicum.ewm.core.interaction.enums.RequestStatus;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateParticipationRequestDto {

    private List<Long> requestsId;

    private RequestStatus status;

}