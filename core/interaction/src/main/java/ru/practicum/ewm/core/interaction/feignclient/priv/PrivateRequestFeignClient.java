package ru.practicum.ewm.core.interaction.feignclient.priv;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.ewm.core.interaction.apiinterface.priv.PrivateRequestOperations;

@FeignClient(name = "requests", path = "/users/{userId}/requests")
public interface PrivateRequestFeignClient extends PrivateRequestOperations {
}
