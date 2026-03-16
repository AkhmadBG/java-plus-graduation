package ru.practicum.ewm.client.stats;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.proto.UserActionControllerGrpc;
import ru.practicum.ewm.stats.proto.UserActionProto;

@Service
public class CollectorClient {

    @GrpcClient("collector")
    private UserActionControllerGrpc.UserActionControllerBlockingStub client;

    public void collectUserAction(UserActionProto action) {
        client.collectUserAction(action);
    }

}