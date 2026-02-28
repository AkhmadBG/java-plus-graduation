package ru.practicum.ewm.core.interaction.feignclient.pub;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.ewm.core.interaction.apiinterface.pub.PublicEventOperations;

@FeignClient(name = "events", contextId = "PublicEvent", path = "/events")
public interface PublicEventFeignClient extends PublicEventOperations {
}
