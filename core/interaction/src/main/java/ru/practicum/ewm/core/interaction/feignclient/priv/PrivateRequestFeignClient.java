package ru.practicum.ewm.core.interaction.feignclient.priv;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.ewm.core.interaction.apiinterface.priv.PrivateRequestOperations;

@FeignClient(name = "requests", contextId = "PrivateRequest", path = "/users")
public interface PrivateRequestFeignClient extends PrivateRequestOperations {
}
