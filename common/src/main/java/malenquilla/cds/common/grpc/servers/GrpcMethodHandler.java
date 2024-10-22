package malenquilla.cds.common.grpc.servers;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import malenquilla.cds.common.exceptions.HTTPException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.Map;

@FunctionalInterface
public interface GrpcMethodHandler {
    Map<HttpStatusCode, Status> EXCEPTION_MAP = Map.of(
            HttpStatus.BAD_REQUEST, Status.ABORTED,
            HttpStatus.NOT_FOUND, Status.NOT_FOUND,
            HttpStatus.UNAUTHORIZED, Status.UNAUTHENTICATED,
            HttpStatus.FORBIDDEN, Status.PERMISSION_DENIED
    );

    void process() throws RuntimeException;

    static void handle(GrpcMethodHandler method, StreamObserver<?> responseObserver) {
        try {
            method.process();
            responseObserver.onCompleted();
        } catch (RuntimeException exception) {
            Status status = Status.INTERNAL;

            if (exception instanceof HTTPException)
                status = EXCEPTION_MAP.get(((HTTPException) exception).getStatusCode())
                                      .withDescription(exception.getMessage());

            responseObserver.onError(status.asRuntimeException());
        }
    }
}
