package ru.practicum.ewm.core.interaction.feignclient.priv;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "events", path = "/users")
public interface PrivateEventFeignClient {
}
