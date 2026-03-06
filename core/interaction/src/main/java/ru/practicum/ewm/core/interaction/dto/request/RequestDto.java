package ru.practicum.ewm.core.interaction.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.practicum.ewm.core.interaction.enums.RequestStatus;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RequestDto {

    private List<Long> requestIds;

    private RequestStatus status;

}