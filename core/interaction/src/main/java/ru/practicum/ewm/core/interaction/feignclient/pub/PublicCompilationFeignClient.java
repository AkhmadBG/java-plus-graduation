package ru.practicum.ewm.core.interaction.feignclient.pub;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.ewm.core.interaction.apiinterface.pub.PublicCompilationOperations;

@FeignClient(name = "events", path = "/compilations")
public interface PublicCompilationFeignClient extends PublicCompilationOperations {
}
