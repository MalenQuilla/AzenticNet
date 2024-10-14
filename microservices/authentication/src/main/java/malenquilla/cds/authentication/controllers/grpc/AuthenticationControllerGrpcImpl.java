package malenquilla.cds.authentication.controllers.grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import malenquilla.cds.authentication.services.AuthenticationService;
import malenquilla.cds.common.grpc.servers.GrpcMethodHandler;
import malenquilla.cds.grpc.proto.AuthoritiesDetailsGrpc;
import malenquilla.cds.grpc.proto.authentication.AuthenticationControllerGrpc;
import malenquilla.cds.grpc.proto.authentication.VerifyAuthenticationRequest;

@RequiredArgsConstructor
public class AuthenticationControllerGrpcImpl extends AuthenticationControllerGrpc.AuthenticationControllerImplBase {
    private final AuthenticationService authenticationService;

    @Override
    public void verifyAuthentication(VerifyAuthenticationRequest request, StreamObserver<AuthoritiesDetailsGrpc> responseObserver) {
        GrpcMethodHandler.handle(() -> {
            AuthoritiesDetailsGrpc authoritiesDetails = this.authenticationService.authenticateHeaderExternal(request.getAccessToken());
            responseObserver.onNext(authoritiesDetails);
        }, responseObserver);
    }
}
