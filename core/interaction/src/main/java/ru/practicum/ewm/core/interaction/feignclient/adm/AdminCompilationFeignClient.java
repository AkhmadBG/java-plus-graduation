package ru.practicum.ewm.core.interaction.feignclient.adm;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.ewm.core.interaction.apiinterface.adm.AdminCompilationOperations;

@FeignClient(name = "event-service", contextId = "AdminCompilation", path = "/admin/compilations")
public interface AdminCompilationFeignClient extends AdminCompilationOperations {
}
