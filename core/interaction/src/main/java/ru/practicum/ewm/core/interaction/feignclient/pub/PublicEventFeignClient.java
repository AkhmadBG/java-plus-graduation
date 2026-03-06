package ru.practicum.ewm.core.interaction.feignclient.pub;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.ewm.core.interaction.apiinterface.pub.PublicEventOperations;

@FeignClient(name = "event-service", contextId = "PublicEvent", path = "/events")
public interface PublicEventFeignClient extends PublicEventOperations {
}