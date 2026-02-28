package ru.practicum.ewm.core.interaction.feignclient.adm;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.ewm.core.interaction.apiinterface.adm.AdminUserOperations;

@FeignClient(name = "users", contextId = "AdminUser", path = "/admin/users")
public interface AdminUserFeignClient extends AdminUserOperations {
}
