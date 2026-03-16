package ru.practicum.ewm.stats.collector.controller;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.practicum.ewm.stats.collector.service.UserActionService;
import ru.practicum.ewm.stats.proto.UserActionControllerGrpc;
import ru.practicum.ewm.stats.proto.UserActionProto;

@GrpcService
@RequiredArgsConstructor
public class UserActionController extends UserActionControllerGrpc.UserActionControllerImplBase {

    private final UserActionService userActionService;

    @Override
    public void collectUserAction(UserActionProto userActionProto, StreamObserver<Empty> responseObserver) {
        userActionService.userActionHandle(userActionProto);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

}