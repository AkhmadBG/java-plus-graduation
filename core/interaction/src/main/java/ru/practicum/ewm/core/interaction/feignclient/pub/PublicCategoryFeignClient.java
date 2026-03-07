package ru.practicum.ewm.core.interaction.feignclient.pub;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.ewm.core.interaction.apiinterface.pub.PublicCategoryOperations;

@FeignClient(name = "event-service", contextId = "PublicCategory", path = "/categories")
public interface PublicCategoryFeignClient extends PublicCategoryOperations {
}