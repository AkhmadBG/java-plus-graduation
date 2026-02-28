package ru.practicum.ewm.core.interaction.feignclient.adm;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.ewm.core.interaction.apiinterface.adm.AdminCompilationOperations;

@FeignClient(name = "events", contextId = "AdminCompilation", path = "/admin/compilations")
public interface AdminCompilationFeignClient extends AdminCompilationOperations {
}
