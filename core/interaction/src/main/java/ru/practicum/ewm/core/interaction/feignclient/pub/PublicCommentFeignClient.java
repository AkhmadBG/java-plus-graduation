package ru.practicum.ewm.core.interaction.feignclient.pub;

import org.springframework.cloud.openfeign.FeignClient;
import ru.practicum.ewm.core.interaction.apiinterface.pub.PublicCommentOperations;

@FeignClient(name = "comment-service", contextId = "PublicComment", path = "/")
public interface PublicCommentFeignClient extends PublicCommentOperations {
}