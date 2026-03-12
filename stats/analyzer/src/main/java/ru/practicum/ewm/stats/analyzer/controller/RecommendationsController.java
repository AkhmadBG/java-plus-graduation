package ru.practicum.ewm.stats.analyzer.controller;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.practicum.ewm.stats.analyzer.service.RecommendationsControllerService;
import ru.practicum.ewm.stats.proto.*;

@GrpcService
@RequiredArgsConstructor
public class RecommendationsController extends RecommendationsControllerGrpc.RecommendationsControllerImplBase {

    private final RecommendationsControllerService recommendationsControllerService;

    @Override
    public RecommendedEventProto getRecommendationsForUser(UserPredictionsRequestProto userPredictionsRequestProto, StreamObserver<Empty> responseObserver) {

        RecommendedEventProto recommendationsForUser = recommendationsControllerService.getRecommendationsForUser(userPredictionsRequestProto);

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
        return recommendationsForUser;
    }

    @Override
    public RecommendedEventProto getSimilarEvents(SimilarEventsRequestProto similarEventsRequestProto, StreamObserver<Empty> responseObserver) {

        RecommendedEventProto similarEvents = recommendationsControllerService.getSimilarEvents(similarEventsRequestProto);

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
        return similarEvents;
    }

    @Override
    public RecommendedEventProto getInteractionsCount(InteractionsCountRequestProto interactionsCountRequestProto, StreamObserver<Empty> responseObserver) {

        RecommendedEventProto interactionsCount = recommendationsControllerService.getInteractionsCount(interactionsCountRequestProto);

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
        return interactionsCount;
    }

}