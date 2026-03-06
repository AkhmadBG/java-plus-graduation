package ru.practicum.ewm.core.interaction.feignclient.adm;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.ewm.core.interaction.apiinterface.adm.AdminRequestOperation;

@FeignClient(name = "requests", contextId = "AdminRequest", path = "/requests")
public interface AdminRequestFeignClient extends AdminRequestOperation {


}
