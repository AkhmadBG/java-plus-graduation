package ru.practicum.ewm.core.interaction.feignclient.priv;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.ewm.core.interaction.apiinterface.priv.PrivateEventOperations;

@FeignClient(name = "events", contextId = "PrivateEvent", path = "/users")
public interface PrivateEventFeignClient extends PrivateEventOperations {
}
