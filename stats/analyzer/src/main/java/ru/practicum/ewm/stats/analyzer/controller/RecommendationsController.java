package ru.practicum.ewm.stats.analyzer.controller;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.practicum.ewm.stats.analyzer.service.RecommendationsControllerService;
import ru.practicum.ewm.stats.proto.*;

@GrpcService
@RequiredArgsConstructor
public class RecommendationsController extends RecommendationsControllerGrpc.RecommendationsControllerImplBase {

    private final RecommendationsControllerService service;

    @Override
    public void getRecommendationsForUser(UserPredictionsRequestProto userPredictionsRequestProto,
                                          StreamObserver<RecommendedEventProto> responseObserver) {
        try {
            service.getRecommendationsForUser(userPredictionsRequestProto).forEach(responseObserver::onNext);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage())
                    .withCause(e)
                    .asRuntimeException());
        }
    }

    @Override
    public void getSimilarEvents(SimilarEventsRequestProto similarEventsRequestProto,
                                 StreamObserver<RecommendedEventProto> responseObserver) {
        try {
            service.getSimilarEvents(similarEventsRequestProto).forEach(responseObserver::onNext);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage())
                    .withCause(e)
                    .asRuntimeException());
        }
    }

    @Override
    public void getInteractionsCount(InteractionsCountRequestProto interactionsCountRequestProto,
                                     StreamObserver<RecommendedEventProto> responseObserver) {
        try {
            service.getInteractionsCount(interactionsCountRequestProto).forEach(responseObserver::onNext);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage())
                    .withCause(e)
                    .asRuntimeException());
        }
    }

}