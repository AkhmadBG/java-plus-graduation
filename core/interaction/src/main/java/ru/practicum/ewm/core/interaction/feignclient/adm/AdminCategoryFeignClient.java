package ru.practicum.ewm.core.interaction.feignclient.adm;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.ewm.core.interaction.apiinterface.adm.AdminCategoryOperations;

@FeignClient(name = "events", contextId = "AdminCategory", path = "/admin/categories")
public interface AdminCategoryFeignClient extends AdminCategoryOperations {
}