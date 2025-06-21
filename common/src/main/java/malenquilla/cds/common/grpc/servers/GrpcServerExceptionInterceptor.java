package malenquilla.cds.common.grpc.servers;

import io.grpc.*;
import malenquilla.cds.common.exceptions.HTTPException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.Map;

public class GrpcServerExceptionInterceptor implements ServerInterceptor {
    private static final Map<HttpStatusCode, Status> EXCEPTION_MAP = Map.of(
        HttpStatus.BAD_REQUEST, Status.ABORTED,
        HttpStatus.NOT_FOUND, Status.NOT_FOUND,
        HttpStatus.UNAUTHORIZED, Status.UNAUTHENTICATED,
        HttpStatus.FORBIDDEN, Status.PERMISSION_DENIED
    );

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
        ServerCall<ReqT, RespT> call,
        Metadata headers,
        ServerCallHandler<ReqT, RespT> next
    ) {
        ServerCall.Listener<ReqT> delegate = next.startCall(call, headers);

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(delegate) {
            @Override
            public void onHalfClose() {
                try {
                    super.onHalfClose();
                } catch (RuntimeException exception) {
                    Status status = Status.INTERNAL;

                    if (exception instanceof HTTPException httpEx) {
                        status = EXCEPTION_MAP.getOrDefault(httpEx.getStatusCode(), Status.INTERNAL)
                                              .withDescription(httpEx.getMessage());
                    }

                    call.close(status, new Metadata());
                }
            }
        };
    }
}
