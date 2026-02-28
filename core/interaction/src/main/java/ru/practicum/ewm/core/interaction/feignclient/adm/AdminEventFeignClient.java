package ru.practicum.ewm.core.interaction.feignclient.adm;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.ewm.core.interaction.apiinterface.adm.AdminEventOperations;

@FeignClient(name = "events", contextId = "AdminEvent", path = "/admin/events")
public interface AdminEventFeignClient extends AdminEventOperations {
}