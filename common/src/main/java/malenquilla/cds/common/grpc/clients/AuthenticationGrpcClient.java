package malenquilla.cds.common.grpc.clients;

import io.grpc.StatusRuntimeException;
import malenquilla.cds.common.exceptions.UnauthorizedException;
import malenquilla.cds.common.security.AuthoritiesAuthentication;
import malenquilla.cds.grpc.proto.AuthoritiesDetailsGrpc;
import malenquilla.cds.grpc.proto.authentication.AuthenticationControllerGrpc;
import malenquilla.cds.grpc.proto.authentication.VerifyAuthenticationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.stream.Collectors;

public class AuthenticationGrpcClient extends AbstractGrpcClient {
    private static String HOST;
    private static int PORT;

    @Value("${cds.grpc.authentication.host}")
    public void setHost(String host) {
        AuthenticationGrpcClient.HOST = host;
    }

    @Value("${cds.grpc.authentication.port}")
    public void setPort(int port) {
        AuthenticationGrpcClient.PORT = port;
    }

    AuthenticationControllerGrpc.AuthenticationControllerBlockingStub blockingStub;

    @Override
    public void initStub() {
        this.blockingStub = AuthenticationControllerGrpc.newBlockingStub(this.initChanel(HOST, PORT)
                                                                             .getChannel());
    }

    public AuthoritiesAuthentication verifyAuthentication(String accessToken) throws StatusRuntimeException {
        if (accessToken == null || accessToken.isEmpty())
            throw new UnauthorizedException();

        VerifyAuthenticationRequest request = VerifyAuthenticationRequest.newBuilder()
                                                                         .setAccessToken(accessToken)
                                                                         .build();
        AuthoritiesDetailsGrpc authoritiesDetails = this.blockingStub.verifyAuthentication(request);

        return new AuthoritiesAuthentication(
                authoritiesDetails.getUserId(),
                authoritiesDetails.getAuthoritiesList()
                                  .stream()
                                  .map(SimpleGrantedAuthority::new)
                                  .collect(Collectors.toSet())
        );
    }
}
