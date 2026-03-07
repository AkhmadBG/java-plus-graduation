package ru.practicum.ewm.core.interaction.feignclient.priv;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.ewm.core.interaction.apiinterface.priv.PrivateCommentOperations;

@FeignClient(name = "comment-service", contextId = "PrivateComment", path = "/")
public interface PrivateCommentFeignClient extends PrivateCommentOperations {
}