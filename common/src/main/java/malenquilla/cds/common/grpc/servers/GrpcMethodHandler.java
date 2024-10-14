package malenquilla.cds.common.grpc.servers;

import io.grpc.stub.StreamObserver;

@FunctionalInterface
public interface GrpcMethodHandler<T> {
    void process() throws RuntimeException;

    static <T> void handle(GrpcMethodHandler<T> method, StreamObserver<T> responseObserver) {
        try {
            method.process();
            responseObserver.onCompleted();
        } catch (RuntimeException exception) {
            responseObserver.onError(exception);
        }
    }
}
